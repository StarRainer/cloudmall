package com.rainer.cloudmail.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rainer.common.utils.PageUtils;
import com.rainer.cloudmail.product.entity.SkuInfoEntity;

import java.util.Map;

/**
 * sku信息
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 11:20:39
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

