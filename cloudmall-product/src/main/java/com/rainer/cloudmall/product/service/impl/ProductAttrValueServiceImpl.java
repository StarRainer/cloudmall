package com.rainer.cloudmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Query;

import com.rainer.cloudmall.product.dao.ProductAttrValueDao;
import com.rainer.cloudmall.product.entity.ProductAttrValueEntity;
import com.rainer.cloudmall.product.service.ProductAttrValueService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Service("productAttrValueService")
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<ProductAttrValueEntity> listAttrBySpuId(Long spuId) {
        return list(new LambdaQueryWrapper<ProductAttrValueEntity>()
                .eq(ProductAttrValueEntity::getSpuId, spuId)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAttrsBySpuId(Long spuId, List<ProductAttrValueEntity> productAttrValueEntities) {
        remove(new LambdaUpdateWrapper<ProductAttrValueEntity>().eq(ProductAttrValueEntity::getSpuId, spuId));
        productAttrValueEntities.forEach(productAttrValueEntity -> productAttrValueEntity.setSpuId(spuId));
        if (CollectionUtils.isEmpty(productAttrValueEntities)) {
            return;
        }
        saveBatch(productAttrValueEntities);
    }

}