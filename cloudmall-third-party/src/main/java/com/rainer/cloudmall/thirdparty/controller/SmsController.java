package com.rainer.cloudmall.thirdparty.controller;

import com.rainer.cloudmall.common.utils.FeignResult;
import com.rainer.cloudmall.thirdparty.service.SmsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
public class SmsController {

    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @GetMapping("/send/code")
    public FeignResult<Void> sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code) {
        smsService.sendCode(phone, code);
        return FeignResult.success();
    }
}
