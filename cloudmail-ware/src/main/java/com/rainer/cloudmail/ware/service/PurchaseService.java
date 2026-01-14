package com.rainer.cloudmail.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rainer.common.utils.PageUtils;
import com.rainer.cloudmail.ware.entity.PurchaseEntity;

import java.util.Map;

/**
 * 采购信息
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 14:41:44
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

