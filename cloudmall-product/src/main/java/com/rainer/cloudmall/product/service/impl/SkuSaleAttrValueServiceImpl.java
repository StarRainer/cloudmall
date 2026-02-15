package com.rainer.cloudmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Query;

import com.rainer.cloudmall.product.dao.SkuSaleAttrValueDao;
import com.rainer.cloudmall.product.entity.SkuSaleAttrValueEntity;
import com.rainer.cloudmall.product.service.SkuSaleAttrValueService;
import org.springframework.util.CollectionUtils;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuSaleAttrValueEntity> getAttrValuesBySkuIds(List<Long> skuIds) {
        return list(new LambdaQueryWrapper<SkuSaleAttrValueEntity>()
                .in(SkuSaleAttrValueEntity::getSkuId, skuIds)
        );
    }

    @Override
    public List<String> getSkuSaleAttrValues(Long skuId) {
        List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = list(new LambdaQueryWrapper<SkuSaleAttrValueEntity>()
                .select(SkuSaleAttrValueEntity::getAttrName, SkuSaleAttrValueEntity::getAttrValue)
                .eq(SkuSaleAttrValueEntity::getSkuId, skuId)
        );
        if (CollectionUtils.isEmpty(skuSaleAttrValueEntities)) {
            return Collections.emptyList();
        }
        return skuSaleAttrValueEntities.stream()
                .map(skuSaleAttrValueEntity ->
                        skuSaleAttrValueEntity.getAttrName() + ":" + skuSaleAttrValueEntity.getAttrValue())
                .toList();
    }

}