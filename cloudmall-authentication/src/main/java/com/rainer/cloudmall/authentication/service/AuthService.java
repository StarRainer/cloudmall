package com.rainer.cloudmall.authentication.service;

import com.rainer.cloudmall.authentication.constant.RedisConstants;
import com.rainer.cloudmall.authentication.exception.SendCodeTooFastException;
import com.rainer.cloudmall.authentication.feign.SmsFeignService;
import com.rainer.cloudmall.common.exception.code.CommonCode;
import com.rainer.cloudmall.common.utils.FeignResult;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AuthService {
    private final SmsFeignService smsFeignService;
    private final StringRedisTemplate stringRedisTemplate;

    public AuthService(SmsFeignService smsFeignService, StringRedisTemplate stringRedisTemplate) {
        this.smsFeignService = smsFeignService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void sendCode(String phone) {

        Boolean locked = stringRedisTemplate.opsForValue().setIfAbsent(
                RedisConstants.CODE_LOCK_KEY_PREFIX + ":" + phone,
                "",
                1,
                TimeUnit.MINUTES
        );
        if (Boolean.FALSE.equals(locked)) {
            throw new SendCodeTooFastException();
        }

        try {
            // 如果5分钟内发送过验证码，需要检查是不是已经发了超过1分钟，如果符合条件再覆盖旧验证码
            String value = stringRedisTemplate.opsForValue().get(RedisConstants.CODE_KEY_PREFIX + ":" + phone);
            if (value != null) {
                String[] split = value.split("_");
                long timestamp = Long.parseLong(split[0]);
                if (System.currentTimeMillis() - timestamp < 60 * 1000) {
                    log.warn("短信发送太频繁：phone={}", phone);
                    throw new SendCodeTooFastException();
                }
            }

            // 生成验证码
            StringBuilder codeBuilder = new StringBuilder();
            SecureRandom random = new SecureRandom();
            for (int i = 0; i < 6; i++) {
                codeBuilder.append(random.nextInt(10));
            }
            String code = codeBuilder.toString();
            try {
                FeignResult<Void> result = smsFeignService.sendCode(phone, code);
                if (result.getCode() == CommonCode.OK.getCode()) {
                    stringRedisTemplate.opsForValue().set(
                            RedisConstants.CODE_KEY_PREFIX + ":" + phone,
                            System.currentTimeMillis()+ "_" + code,
                            5,
                            TimeUnit.MINUTES
                    );
                    log.info("发送短信成功：phone={}", phone);
                } else {
                    log.warn("发送短信业务失败：phone={}, reason={}", phone, result.getMsg());
                }

            } catch (FeignException e) {
                log.warn("发送短信请求异常，HTTP状态码：{}", e.status());
                log.error("发送短信详细失败原因：content={}", e.contentUTF8());
            }
        } finally {
            stringRedisTemplate.delete(RedisConstants.CODE_LOCK_KEY_PREFIX + ":" + phone);
        }
    }
}
