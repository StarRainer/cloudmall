package com.rainer.cloudmall.authentication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainer.cloudmall.authentication.feign.SmsFeignService;
import com.rainer.cloudmall.common.exception.code.CommonCode;
import com.rainer.cloudmall.common.utils.FeignResult;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class AuthService {
    private final SmsFeignService smsFeignService;

    public AuthService(SmsFeignService smsFeignService) {
        this.smsFeignService = smsFeignService;
    }

    public void sendCode(String phone) {
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 6; i++) {
            codeBuilder.append(random.nextInt(10));
        }
        String code = codeBuilder.toString();

        try {
            FeignResult<Void> result = smsFeignService.sendCode(phone, code);
            if (result.getCode() == CommonCode.OK.getCode()) {
                log.info("发送短信成功：phone={}", phone);
            } else {
                log.warn("发送短信业务失败：phone={}, reason={}", phone, result.getMsg());
            }

        } catch (FeignException e) {
            log.warn("发送短信请求异常，HTTP状态码：{}", e.status());
            log.error("发送短信详细失败原因：content={}", e.contentUTF8());
        }
    }
}
