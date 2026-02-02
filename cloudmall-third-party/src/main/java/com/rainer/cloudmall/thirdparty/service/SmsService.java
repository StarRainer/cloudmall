package com.rainer.cloudmall.thirdparty.service;

import com.rainer.cloudmall.thirdparty.config.AliSmsProperties;
import com.rainer.cloudmall.thirdparty.vo.SmsResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class SmsService {

    private final RestClient restClient;
    private final AliSmsProperties aliSmsProperties;

    public SmsService(RestClient.Builder builder, AliSmsProperties aliSmsProperties) {
        this.restClient = builder
                .baseUrl(aliSmsProperties.getUrl())
                .defaultHeader("Authorization", "APPCODE " + aliSmsProperties.getAppCode())
                .build();
        this.aliSmsProperties = aliSmsProperties;
    }

    public void sendCode(String phone, String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("content", "code:" + code + ",expire_at:" + aliSmsProperties.getExpire());
        formData.add("template_id", aliSmsProperties.getTemplateId());
        formData.add("phone_number", phone);

        log.debug("向第三方短信服务发送请求：{}", formData);
        SmsResponseVo result = restClient.post()
                .uri("/data/send_sms")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(SmsResponseVo.class);

        if (result != null) {
            log.info("短信发送成功：request-id={}, phone={}", result.getRequestId(), phone);
        }
    }
}