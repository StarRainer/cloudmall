package com.rainer.cloudmall.thirdparty.feign;

import com.rainer.cloudmall.thirdparty.vo.SmsResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "aliyun-sms", url = "https://dfsns.market.alicloudapi.com")
public interface SmsFeignService {

    @PostMapping(
            value = "/data/send_sms",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    SmsResponseVo sendCode(
            @RequestHeader("Authorization") String authorization,
            @RequestBody MultiValueMap<String, String> paramMap
    );

}
