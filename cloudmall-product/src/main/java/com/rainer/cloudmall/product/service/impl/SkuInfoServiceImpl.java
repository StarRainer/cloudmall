package com.rainer.cloudmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Query;
import com.rainer.cloudmall.product.dao.SkuInfoDao;
import com.rainer.cloudmall.product.entity.SkuImagesEntity;
import com.rainer.cloudmall.product.entity.SkuInfoEntity;
import com.rainer.cloudmall.product.entity.SpuInfoDescEntity;
import com.rainer.cloudmall.product.entity.SpuInfoEntity;
import com.rainer.cloudmall.product.service.SkuImagesService;
import com.rainer.cloudmall.product.service.SkuInfoService;
import com.rainer.cloudmall.product.service.SpuInfoDescService;
import com.rainer.cloudmall.product.vo.SkuItemVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    private final SkuImagesService skuImagesService;
    private final SpuInfoDescService spuInfoDescService;

    public SkuInfoServiceImpl(SkuImagesService skuImagesService, SpuInfoDescService spuInfoDescService) {
        this.skuImagesService = skuImagesService;
        this.spuInfoDescService = spuInfoDescService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        String cateLogIdText = (String) params.get("cateLogId");
        String brandIdText = (String) params.get("brandId");
        String minText = (String) params.get("min");
        String maxText = (String) params.get("max");

        LambdaQueryWrapper<SkuInfoEntity> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasLength(cateLogIdText)) {
            long cateLogId = Long.parseLong(cateLogIdText);
            wrapper.eq(cateLogId != 0, SkuInfoEntity::getCatalogId, cateLogId);
        }

        if (StringUtils.hasLength(brandIdText)) {
            long brandId = Long.parseLong(brandIdText);
            wrapper.eq(brandId != 0, SkuInfoEntity::getBrandId, brandId);
        }

        if (StringUtils.hasLength(minText)) {
            wrapper.ge(SkuInfoEntity::getPrice, new BigDecimal(minText));
        }

        if (StringUtils.hasLength(maxText)) {
            BigDecimal max = new BigDecimal(maxText);
            wrapper.le(max.compareTo(BigDecimal.ZERO) > 0, SkuInfoEntity::getPrice, max);
        }

        if (StringUtils.hasLength(key)) {
            boolean isNumber = key.matches("^\\d+$");
            wrapper.and(w -> {
                if (isNumber) {
                    w.eq(SkuInfoEntity::getSkuId, Long.parseLong(key));
                }
                w.or().like(SkuInfoEntity::getSkuName, key);
            });
        }

        return new PageUtils(page(new Query<SkuInfoEntity>().getPage(params), wrapper));
    }

    @Override
    public Map<Long, String> getSkuNamesBySkuIds(List<Long> skuIds) {
        if (CollectionUtils.isEmpty(skuIds)) {
            return Collections.emptyMap();
        }
        return list(new LambdaQueryWrapper<SkuInfoEntity>()
                .select(SkuInfoEntity::getSkuId, SkuInfoEntity::getSkuName)
                .in(SkuInfoEntity::getSkuId, skuIds)
        ).stream().collect(Collectors.toMap(SkuInfoEntity::getSkuId, SkuInfoEntity::getSkuName));
    }

    @Override
    public List<SkuInfoEntity> getSkuInfosBySpuId(Long spuId) {
        return list(new LambdaQueryWrapper<SkuInfoEntity>()
                .eq(SkuInfoEntity::getSpuId, spuId)
        );
    }

    @Override
    public List<Long> getSkuIdsByspuId(Long spuId) {
        return list(new LambdaQueryWrapper<SkuInfoEntity>()
                .select(SkuInfoEntity::getSkuId)
                .eq(SkuInfoEntity::getSpuId, spuId)
        ).stream().map(SkuInfoEntity::getSkuId).toList();
    }
}