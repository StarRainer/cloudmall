package com.rainer.cloudmall.authentication.service;

import com.rainer.cloudmall.authentication.constant.RedisConstants;
import com.rainer.cloudmall.authentication.exception.SendCodeFailureException;
import com.rainer.cloudmall.authentication.exception.SendCodeTooFastException;
import com.rainer.cloudmall.authentication.feign.MemberFeignService;
import com.rainer.cloudmall.authentication.feign.SmsFeignService;
import com.rainer.cloudmall.authentication.vo.UserLoginVo;
import com.rainer.cloudmall.authentication.vo.UserRegisterVo;
import com.rainer.cloudmall.common.constant.SessionConstants;
import com.rainer.cloudmall.common.exception.code.CommonCode;
import com.rainer.cloudmall.common.utils.FeignResult;
import com.rainer.cloudmall.common.vo.UserVo;
import feign.FeignException;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AuthService {
    private final SmsFeignService smsFeignService;
    private final StringRedisTemplate stringRedisTemplate;
    private final MemberFeignService memberFeignService;

    public AuthService(SmsFeignService smsFeignService, StringRedisTemplate stringRedisTemplate, MemberFeignService memberFeignService) {
        this.smsFeignService = smsFeignService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.memberFeignService = memberFeignService;
    }

    public void sendCode(String phone) {
        String lockKey = RedisConstants.CODE_LOCK_KEY_PREFIX + ":" + phone;

        Boolean lockedResult = stringRedisTemplate.opsForValue().setIfAbsent(
                RedisConstants.CODE_LOCK_KEY_PREFIX + ":" + phone,
                "",
                10,
                TimeUnit.SECONDS
        );
        if (Boolean.FALSE.equals(lockedResult)) {
            throw new SendCodeTooFastException();
        }

        try {
            String limitKey = RedisConstants.CODE_LIMIT_KEY_PREFIX + ":" + phone;
            String codeKey = RedisConstants.CODE_KEY_PREFIX + ":" + phone;

            // 如果5分钟内发送过验证码，需要检查是不是已经发了超过1分钟，如果符合条件再覆盖旧验证码
            String stampValue = stringRedisTemplate.opsForValue().get(limitKey);
            if (stampValue != null) {
                long timestamp = Long.parseLong(stampValue);
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
            FeignResult<Void> result = smsFeignService.sendCode(phone, code);
            if (result.getCode() == CommonCode.OK.getCode()) {
                stringRedisTemplate.opsForValue().set(
                        codeKey,
                        code,
                        5,
                        TimeUnit.MINUTES
                );
                stringRedisTemplate.opsForValue().set(
                        limitKey,
                        String.valueOf(System.currentTimeMillis()),
                        5,
                        TimeUnit.MINUTES
                );
                log.info("发送短信成功：phone={}", phone);
            } else {
                log.warn("发送短信业务失败：phone={}, reason={}", phone, result.getMsg());
                throw new SendCodeFailureException();
            }
        } finally {
            stringRedisTemplate.delete(lockKey);
        }
    }

    public String register(UserRegisterVo userRegisterVo, RedirectAttributes redirectAttributes) {
        String lockKey = RedisConstants.CODE_LOCK_KEY_PREFIX + ":" + userRegisterVo.getPhone();
        Boolean lockedResult = stringRedisTemplate.opsForValue().setIfAbsent(lockKey,
                "",
                10,
                TimeUnit.SECONDS
        );
        if (Boolean.FALSE.equals(lockedResult)) {
            throw new SendCodeTooFastException("操作过于频繁，请稍后再试");
        }
        Map<String, String> errors = new HashMap<>();
        try {
            String codeKey = RedisConstants.CODE_KEY_PREFIX + ":" + userRegisterVo.getPhone();
            String code = stringRedisTemplate.opsForValue().get(codeKey);
            if (StringUtils.hasLength(code)) {
                if (code.equals(userRegisterVo.getCode())) {
                    try {
                        FeignResult<Void> feignResult = memberFeignService.register(userRegisterVo);
                        if (feignResult.getCode() == CommonCode.OK.getCode()) {
                            log.info("注册账号成功：phone={}, username={}", userRegisterVo.getPhone(), userRegisterVo.getUserName());
                            return "redirect:http://auth.cloudmall.com/login.html";
                        } else {
                            log.warn("注册失败：message={}", feignResult.getMsg());
                            errors.put("msg", feignResult.getMsg());
                        }
                    } catch (FeignException e) {
                        log.warn("远程调用失败...");
                        log.warn("message={}", e.getMessage());
                        errors.put("msg", e.getMessage());
                    }
                } else {
                    errors.put("code", "验证码错误");
                }
            } else {
                errors.put("code", "验证码错误");
            }
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.cloudmall.com/reg.html";
        } finally {
            stringRedisTemplate.delete(lockKey);
        }
    }

    public String login(UserLoginVo loginVo, RedirectAttributes redirectAttributes, HttpSession session) {
        Map<String, String> errors = new HashMap<>();
        try {
            FeignResult<UserVo> result = memberFeignService.login(loginVo);
            if (result.getCode() == CommonCode.OK.getCode()) {
                log.info("登录成功：account={}", loginVo.getLoginacct());
                session.setAttribute(SessionConstants.LOGIN_USER, result.getData());
                return "redirect:http://cloudmall.com";
            } else {
                errors.put("msg", result.getMsg());
            }
        } catch (FeignException e) {
            errors.put("msg", e.getMessage());
            log.warn("远程调用失败：message={}", e.getMessage());
        }
        redirectAttributes.addFlashAttribute("errors", errors);
        return "redirect:http://auth.cloudmall.com/login.html";
    }
}
