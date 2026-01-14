package com.rainer.cloudmail.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rainer.common.utils.PageUtils;
import com.rainer.cloudmail.product.entity.AttrEntity;

import java.util.Map;

/**
 * 商品属性
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 11:20:39
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

