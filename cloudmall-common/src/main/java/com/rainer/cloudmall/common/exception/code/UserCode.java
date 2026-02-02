package com.rainer.cloudmall.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserCode implements IResponseCode {
    USERNAME_EXIST_EXCEPTION(15001, "该用户名已存在"),
    PHONE_EXIST_EXCEPTION(15002, "该手机号已注册");

    private final int code;
    private final String message;
}
