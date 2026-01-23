package com.rainer.cloudmall.product.feign;

import com.rainer.cloudmall.common.to.SkuReductionTo;
import com.rainer.cloudmall.common.to.SpuBoundsTo;
import com.rainer.cloudmall.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "cloudmall-coupon")
public interface CouponFeignService {
    @PostMapping("/coupon/spubounds/save")
    Result saveSpuBounds(@RequestBody SpuBoundsTo spuBoundsTo);

    @PostMapping("/coupon/skufullreduction/save/batch")
    Result saveSkuReductions(@RequestBody List<SkuReductionTo> skuReductionTos);
}
