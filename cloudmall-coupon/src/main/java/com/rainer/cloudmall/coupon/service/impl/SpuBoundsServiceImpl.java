package com.rainer.cloudmall.coupon.service.impl;

import com.rainer.cloudmall.common.to.SpuBoundsTo;
import com.rainer.cloudmall.coupon.utils.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Query;

import com.rainer.cloudmall.coupon.dao.SpuBoundsDao;
import com.rainer.cloudmall.coupon.entity.SpuBoundsEntity;
import com.rainer.cloudmall.coupon.service.SpuBoundsService;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service("spuBoundsService")
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class SpuBoundsServiceImpl extends ServiceImpl<SpuBoundsDao, SpuBoundsEntity> implements SpuBoundsService {

    private final ProductMapper productMapper;

    public SpuBoundsServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuBoundsEntity> page = this.page(
                new Query<SpuBoundsEntity>().getPage(params),
                new QueryWrapper<SpuBoundsEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SpuBoundsTo spuBoundsTo) {
        SpuBoundsEntity spuBoundsEntity = productMapper.spuBoundsToToSpuBoundsEntity(spuBoundsTo);
        save(spuBoundsEntity);
        log.debug("spuBoundsEntity={}", spuBoundsEntity);
    }

}