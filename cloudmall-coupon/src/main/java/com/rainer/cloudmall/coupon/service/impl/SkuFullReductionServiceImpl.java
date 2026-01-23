package com.rainer.cloudmall.coupon.service.impl;

import com.rainer.cloudmall.common.to.SkuReductionTo;
import com.rainer.cloudmall.coupon.entity.MemberPriceEntity;
import com.rainer.cloudmall.coupon.entity.SkuLadderEntity;
import com.rainer.cloudmall.coupon.service.MemberPriceService;
import com.rainer.cloudmall.coupon.service.SkuLadderService;
import com.rainer.cloudmall.coupon.utils.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Query;

import com.rainer.cloudmall.coupon.dao.SkuFullReductionDao;
import com.rainer.cloudmall.coupon.entity.SkuFullReductionEntity;
import com.rainer.cloudmall.coupon.service.SkuFullReductionService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Slf4j
@Service("skuFullReductionService")
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    private final ProductMapper productMapper;
    private final SkuLadderService skuLadderService;
    private final MemberPriceService memberPriceService;

    public SkuFullReductionServiceImpl(ProductMapper productMapper, SkuLadderService skuLadderService, MemberPriceService memberPriceService) {
        this.productMapper = productMapper;
        this.skuLadderService = skuLadderService;
        this.memberPriceService = memberPriceService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSkuReductions(List<SkuReductionTo> skuReductionTos) {
        if (CollectionUtils.isEmpty(skuReductionTos)) {
            return;
        }
        List<SkuLadderEntity> skuLadderEntities = new ArrayList<>();
        List<SkuFullReductionEntity> skuFullReductionEntities = new ArrayList<>();
        List<MemberPriceEntity> memberPriceEntities = new ArrayList<>();
        skuReductionTos.forEach(skuReductionTo -> {
            SkuLadderEntity skuLadderEntity = productMapper.skuReductionToTOSkuLadderEntity(skuReductionTo);
            if (skuLadderEntity != null && skuLadderEntity.getFullCount() > 0) {
                skuLadderEntities.add(skuLadderEntity);
            }
            SkuFullReductionEntity skuFullReductionEntity = productMapper.skuReductionToToSkuFullReductionEntity(skuReductionTo);
            if (skuFullReductionEntity != null && skuFullReductionEntity.getFullPrice().compareTo(BigDecimal.ZERO) > 0) {
                skuFullReductionEntities.add(skuFullReductionEntity);
            }
            List<MemberPriceEntity> currentMemberPriceEntities = productMapper
                    .memberPriceTosToMemberPriceEntities(
                            skuReductionTo.getMemberPrice(),
                            skuReductionTo.getSkuId()
                    )
                    .stream()
                    .filter(entity -> entity != null && entity.getMemberPrice().compareTo(BigDecimal.ZERO) > 0)
                    .toList();
            memberPriceEntities.addAll(currentMemberPriceEntities);
        });
        skuLadderService.saveBatch(skuLadderEntities);
        log.debug("skuLadderEntities:{}", skuLadderEntities);
        saveBatch(skuFullReductionEntities);
        log.debug("skuFullReductionEntities:{}", skuFullReductionEntities);
        memberPriceService.saveBatch(memberPriceEntities);
        log.debug("memberPriceEntities:{}", memberPriceEntities);
    }

}