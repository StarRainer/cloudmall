package com.rainer.cloudmall.cart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainer.cloudmall.cart.constants.RedisConstants;
import com.rainer.cloudmall.cart.feign.ProductFeignService;
import com.rainer.cloudmall.cart.to.UserInfoTo;
import com.rainer.cloudmall.cart.utils.UserContext;
import com.rainer.cloudmall.cart.vo.CartItemVo;
import com.rainer.cloudmall.cart.vo.CartVo;
import com.rainer.cloudmall.cart.vo.SkuInfoVo;
import com.rainer.cloudmall.common.exception.CommonException;
import com.rainer.cloudmall.common.exception.code.CommonCode;
import com.rainer.cloudmall.common.utils.FeignResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Service
public class CartService {
    private final ProductFeignService productFeignService;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate stringRedisTemplate;

    public CartService(ProductFeignService productFeignService, ThreadPoolExecutor threadPoolExecutor, ObjectMapper objectMapper, StringRedisTemplate stringRedisTemplate) {
        this.productFeignService = productFeignService;
        this.threadPoolExecutor = threadPoolExecutor;
        this.objectMapper = objectMapper;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void addCartItem(Long skuId, Integer num) {
        String cartKey = getCartKey();
        BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps(cartKey);
        String jsonCartItem = (String) hashOps.get(skuId.toString());
        if (StringUtils.hasLength(jsonCartItem)) {
            try {
                CartItemVo cartItemVo = objectMapper.readValue(jsonCartItem, new TypeReference<CartItemVo>() {});
                cartItemVo.setCount(cartItemVo.getCount() + num);
                hashOps.put(skuId.toString(), objectMapper.writeValueAsString(cartItemVo));
                return;
            } catch (JsonProcessingException e) {
                throw new CommonException(e.getMessage());
            }
        }

        CartItemVo cartItemVo = new CartItemVo();
        CompletableFuture<Void> getSkuInfoTask = CompletableFuture.runAsync(() -> {
            FeignResult<SkuInfoVo> feignResult = productFeignService.getSkuInfo(skuId);
            if (feignResult == null || feignResult.getCode() != CommonCode.OK.getCode() || feignResult.getData() == null) {
                throw new CommonException("远程查询sku详情失败：skuId=%s, feignResult=%s".formatted(skuId, feignResult));
            }
            SkuInfoVo skuInfoVo = feignResult.getData();
            cartItemVo.setSkuId(skuId);
            cartItemVo.setCheck(true);
            cartItemVo.setTitle(skuInfoVo.getSkuTitle());
            cartItemVo.setImage(skuInfoVo.getSkuDefaultImg());
            cartItemVo.setPrice(skuInfoVo.getPrice());
            cartItemVo.setCount(num);
        }, threadPoolExecutor);

        CompletableFuture<Void> getSkuSaleAttrTask = CompletableFuture.runAsync(() -> {
            FeignResult<List<String>> feignResult = productFeignService.getSkuSaleAttrValues(skuId);
            if (feignResult == null || feignResult.getCode() != CommonCode.OK.getCode()) {
                throw new CommonException("远程查询sku属性值失败：skuId=%s, feignResult=%s".formatted(skuId, feignResult));
            }
            cartItemVo.setSkuAttrValues(feignResult.getData());
        }, threadPoolExecutor);


        try {
            CompletableFuture.allOf(getSkuInfoTask, getSkuSaleAttrTask).get();
        } catch (ExecutionException e) {
            throw new CommonException(e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CommonException("系统繁忙，线程被中断");
        }

        try {
            hashOps.put(skuId.toString(), objectMapper.writeValueAsString(cartItemVo));
        } catch (JsonProcessingException e) {
            throw new CommonException(e.getMessage());
        }
    }

    private String getCartKey() {
        UserInfoTo userInfoTo = UserContext.get();
        if (userInfoTo.getUserId() == null) {
            return RedisConstants.CART_kEY_PREFIX + userInfoTo.getUserKey();
        }
        return RedisConstants.CART_kEY_PREFIX + userInfoTo.getUserId();
    }

    public CartItemVo getCartItem(Long skuId) {
        String cartKey = getCartKey();
        BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps(cartKey);
        String jsonCartItem = (String) hashOps.get(skuId.toString());
        if (!StringUtils.hasLength(jsonCartItem)) {
            return null;
        }
        try {
            return objectMapper.readValue(jsonCartItem, new TypeReference<CartItemVo>() {});
        } catch (JsonProcessingException e) {
            throw new CommonException(e.getMessage());
        }
    }

    public CartVo getCart() {
        CartVo cartVo = new CartVo();
        if (UserContext.isLogin()) {
            mergeTempCart(cartVo);
            return cartVo;
        }
        String cartKey = getCartKey();
        BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps(cartKey);
        List<Object> values = hashOps.values();
        if (!CollectionUtils.isEmpty(values)) {
            Map<String, CartItemVo> cartMap = getCartMap(hashOps);
            cartVo.setItems(cartMap.values().stream().toList());
        }
        return cartVo;
    }

    private void mergeTempCart(CartVo cartVo) {
        String tempCartKey = RedisConstants.CART_kEY_PREFIX + UserContext.get().getUserKey();
        String loginCartKey = RedisConstants.CART_kEY_PREFIX + UserContext.get().getUserId();
        BoundHashOperations<String, Object, Object> tempHashOps = stringRedisTemplate.boundHashOps(tempCartKey);
        BoundHashOperations<String, Object, Object> loginHashOps = stringRedisTemplate.boundHashOps(loginCartKey);

        // 获取购物车
        Map<String, CartItemVo> tempCartMap = getCartMap(tempHashOps);
        Map<String, CartItemVo> loginCartMap = getCartMap(loginHashOps);

        if (CollectionUtils.isEmpty(tempCartMap)) {
            cartVo.setItems(loginCartMap.values().stream().toList());
            return;
        }

        // 遍历临时购物车，合并数据
        tempCartMap.forEach((skuId, tempItem) -> {
            if (loginCartMap.containsKey(skuId)) {
                // 情况一：登录购物车里也有 -> 累加数量
                CartItemVo loginItem = loginCartMap.get(skuId);
                loginItem.setCount(loginItem.getCount() + tempItem.getCount());
            } else {
                // 情况二：登录购物车里没有 -> 新增条目
                loginCartMap.put(skuId, tempItem);
            }
        });

        // 准备存入 Redis 的数据
        Map<String, String> finalDataToSave = loginCartMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            try {
                                return objectMapper.writeValueAsString(entry.getValue());
                            } catch (JsonProcessingException e) {
                                throw new CommonException("序列化失败");
                            }
                        }
                ));

        // 删除临时购物车
        stringRedisTemplate.delete(tempCartKey);

        // 批量保存合并后的数据
        loginHashOps.putAll(finalDataToSave);

        // 返回购物车信息
        cartVo.setItems(loginCartMap.values().stream().toList());
    }

    private Map<String, CartItemVo> getCartMap(BoundHashOperations<String, Object, Object> hashOps) {
        Map<Object, Object> entries = hashOps.entries();

        if (CollectionUtils.isEmpty(entries)) {
            return Collections.emptyMap();
        }

        return entries.entrySet().stream()
                .map(entry -> {
                    try {
                        String jsonValue = String.valueOf(entry.getValue());
                        CartItemVo item = objectMapper.readValue(jsonValue, new TypeReference<CartItemVo>() {});
                        return new AbstractMap.SimpleEntry<>(String.valueOf(entry.getKey()), item);
                    } catch (JsonProcessingException e) {
                        log.error("JSON解析失败", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void checkItem(Long skuId, Integer checked) {
        String cartKey = getCartKey();
        BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps(cartKey);

        // 获取商品
        CartItemVo cartItem = getCartItem(skuId);

        // 判空
        if (cartItem == null) {
            throw new CommonException("购物车无此商品");
        }

        // 修改状态
        cartItem.setCheck(checked == 1);

        // 写回 Redis
        try {
            hashOps.put(skuId.toString(), objectMapper.writeValueAsString(cartItem));
        } catch (JsonProcessingException e) {
            throw new CommonException(e.getMessage());
        }
    }

    public void countItem(Long skuId, Integer num) {
        String cartKey = getCartKey();
        BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps(cartKey);

        // 获取商品
        CartItemVo cartItem = getCartItem(skuId);

        // 判空
        if (cartItem == null) {
            throw new CommonException("购物车无此商品");
        }

        // 修改状态
        cartItem.setCount(num);

        // 写回 Redis
        try {
            hashOps.put(skuId.toString(), objectMapper.writeValueAsString(cartItem));
        } catch (JsonProcessingException e) {
            throw new CommonException(e.getMessage());
        }
    }

    public void deleteItem(Long skuId) {
        stringRedisTemplate.opsForHash().delete(getCartKey(), skuId.toString());
    }
}
