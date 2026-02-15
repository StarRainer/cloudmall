package com.rainer.cloudmall.cart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainer.cloudmall.cart.constants.RedisConstants;
import com.rainer.cloudmall.cart.feign.ProductFeignService;
import com.rainer.cloudmall.cart.to.UserInfoTo;
import com.rainer.cloudmall.cart.utils.UserContext;
import com.rainer.cloudmall.cart.vo.CartItemVo;
import com.rainer.cloudmall.cart.vo.SkuInfoVo;
import com.rainer.cloudmall.common.exception.CommonException;
import com.rainer.cloudmall.common.exception.code.CommonCode;
import com.rainer.cloudmall.common.utils.FeignResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;


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
}
