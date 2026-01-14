package com.rainer.cloudmail.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rainer.common.utils.PageUtils;
import com.rainer.cloudmail.coupon.entity.CouponSpuCategoryRelationEntity;

import java.util.Map;

/**
 * 优惠券分类关联
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 14:39:15
 */
public interface CouponSpuCategoryRelationService extends IService<CouponSpuCategoryRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

