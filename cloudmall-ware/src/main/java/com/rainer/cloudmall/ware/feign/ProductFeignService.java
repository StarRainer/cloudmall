package com.rainer.cloudmall.ware.feign;

import com.rainer.cloudmall.common.utils.FeignResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "cloudmall-product")
public interface ProductFeignService {
    @PostMapping("/product/skuinfo/skuname")
    FeignResult<Map<Long, String>> getSkuNamesBySkuIds(@RequestBody List<Long> skuIds);
}
