package com.rainer.cloudmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.product.entity.SkuInfoEntity;
import com.rainer.cloudmall.product.vo.SkuItemVo;

import java.util.List;
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

    Map<Long, String> getSkuNamesBySkuIds(List<Long> skuIds);

    List<SkuInfoEntity> getSkuInfosBySpuId(Long spuId);

    List<Long> getSkuIdsByspuId(Long spuId);
}

