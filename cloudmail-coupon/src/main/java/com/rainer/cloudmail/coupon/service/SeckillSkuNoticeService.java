package com.rainer.cloudmail.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rainer.common.utils.PageUtils;
import com.rainer.cloudmail.coupon.entity.SeckillSkuNoticeEntity;

import java.util.Map;

/**
 * 秒杀商品通知订阅
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 14:39:14
 */
public interface SeckillSkuNoticeService extends IService<SeckillSkuNoticeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

