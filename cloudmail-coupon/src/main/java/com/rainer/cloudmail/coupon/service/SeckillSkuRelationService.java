package com.rainer.cloudmail.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rainer.common.utils.PageUtils;
import com.rainer.cloudmail.coupon.entity.SeckillSkuRelationEntity;

import java.util.Map;

/**
 * 秒杀活动商品关联
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 14:39:14
 */
public interface SeckillSkuRelationService extends IService<SeckillSkuRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

