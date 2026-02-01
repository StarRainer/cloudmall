package com.rainer.cloudmall.product.service;

import com.rainer.cloudmall.product.vo.SkuItemVo;

public interface SkuManager {
    SkuItemVo getItemDetail(Long skuId);
}
