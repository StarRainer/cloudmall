package com.rainer.cloudmall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainer.cloudmall.common.exception.code.CommonCode;
import com.rainer.cloudmall.common.utils.FeignResult;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Query;
import com.rainer.cloudmall.ware.dao.WareSkuDao;
import com.rainer.cloudmall.ware.entity.PurchaseDetailEntity;
import com.rainer.cloudmall.ware.entity.WareSkuEntity;
import com.rainer.cloudmall.ware.feign.ProductFeignService;
import com.rainer.cloudmall.ware.service.WareSkuService;
import com.rainer.cloudmall.ware.utils.WareMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service("wareSkuService")
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    private final WareSkuDao wareSkuDao;
    private final WareMapper wareMapper;
    private final ProductFeignService productFeignService;

    public WareSkuServiceImpl(WareSkuDao wareSkuDao, WareMapper wareMapper, ProductFeignService productFeignService) {
        this.wareSkuDao = wareSkuDao;
        this.wareMapper = wareMapper;
        this.productFeignService = productFeignService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WareSkuEntity> wrapper = new LambdaQueryWrapper<>();

        String key = (String) params.get("key");
        String skuIdText = (String) params.get("skuId");
        String wareIdText = (String) params.get("wareId");

        if (StringUtils.hasLength(skuIdText)) {
            long skuId = Long.parseLong(skuIdText);
            wrapper.eq(skuId != 0, WareSkuEntity::getSkuId, skuId);
        }

        if (StringUtils.hasLength(wareIdText)) {
            long wareId = Long.parseLong(wareIdText);
            wrapper.eq(wareId != 0, WareSkuEntity::getWareId, wareId);
        }

        if (StringUtils.hasLength(key)) {
            if (key.matches("^\\d+$")) {
                wrapper.eq(WareSkuEntity::getId, Long.parseLong(key));
            }
            wrapper.or().like(WareSkuEntity::getSkuName, key);
        }

        return new PageUtils(page(
                new Query<WareSkuEntity>().getPage(params),
                wrapper
        ));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStockByPurchase(List<PurchaseDetailEntity> purchaseDetailEntities) {
        if (CollectionUtils.isEmpty(purchaseDetailEntities)) {
            return;
        }
        List<WareSkuEntity> wareSkuEntities = wareMapper.toWareSkuEntities(purchaseDetailEntities);
        List<Long> skuIds = wareSkuEntities.stream().map(WareSkuEntity::getSkuId).toList();

        Map<Long, String> skuIdToSkuName = new HashMap<>();
        try {
            FeignResult<Map<Long, String>> feignResult = productFeignService.getSkuNamesBySkuIds(skuIds);
            if (feignResult.getCode() == CommonCode.OK.getCode()) {
                Map<Long, String> data = feignResult.getData();
                if (data != null) {
                    skuIdToSkuName.putAll(data);
                }
                log.debug("feignResult.data: {}", data);
            } else {
                log.warn("远程查询SKU名称失败，Code: {}, Msg: {}", feignResult.getCode(), feignResult.getMsg());
            }
        } catch (Exception e) {
            log.warn("远程查询SKU名称服务异常，本次入库将不包含商品名称", e);
        }

        wareSkuEntities.forEach(wareSkuEntity -> {
            wareSkuEntity.setSkuName(skuIdToSkuName.get(wareSkuEntity.getSkuId()));
            wareSkuEntity.setStockLocked(0);
        });
        wareSkuDao.addStock(wareSkuEntities);
    }

}