package com.rainer.cloudmall.authentication.controller;

import com.rainer.cloudmall.authentication.service.AuthService;
import com.rainer.cloudmall.common.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/sms/sendCode")
    public Result sendCode(@RequestParam("phone") String phone) {
        authService.sendCode(phone);
        return Result.ok();
    }
}
