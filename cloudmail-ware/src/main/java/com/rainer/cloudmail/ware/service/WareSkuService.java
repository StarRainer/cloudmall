package com.rainer.cloudmail.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rainer.common.utils.PageUtils;
import com.rainer.cloudmail.ware.entity.WareSkuEntity;

import java.util.Map;

/**
 * 商品库存
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 14:41:44
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

