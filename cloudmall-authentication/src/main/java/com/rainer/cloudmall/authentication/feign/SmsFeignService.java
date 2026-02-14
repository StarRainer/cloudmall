package com.rainer.cloudmall.authentication.feign;

import com.rainer.cloudmall.common.utils.FeignResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cloudmall-third-party")
public interface SmsFeignService {
    @GetMapping("/sms/send/code")
    FeignResult<Void> sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code);
}
