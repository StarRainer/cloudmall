package com.rainer.cloudmall.cart.feign;

import com.rainer.cloudmall.cart.vo.SkuInfoVo;
import com.rainer.cloudmall.common.utils.FeignResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("cloudmall-product")
public interface ProductFeignService {
    @RequestMapping("/product/skuinfo/skuinfo/{skuId}")
    FeignResult<SkuInfoVo> getSkuInfo(@PathVariable("skuId") Long skuId);

    @GetMapping("/product/skusaleattrvalue/skuSaleAttrValues/{skuId}")
    FeignResult<List<String>> getSkuSaleAttrValues(@PathVariable("skuId") Long skuId);
}
