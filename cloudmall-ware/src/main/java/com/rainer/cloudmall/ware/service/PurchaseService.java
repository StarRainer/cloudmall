package com.rainer.cloudmall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.ware.entity.PurchaseEntity;
import com.rainer.cloudmall.ware.vo.FinishPurchaseVo;
import com.rainer.cloudmall.ware.vo.MergePurchaseVo;

import java.util.List;
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

    PageUtils queryUnreceivePurchasePage(Map<String, Object> params);

    void mergePurchase(MergePurchaseVo mergePurchaseVo);

    void receivePurchase(List<Long> purchaseIds);

    void finishPurchase(FinishPurchaseVo finishPurchaseVo);
}

