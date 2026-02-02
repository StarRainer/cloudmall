package com.rainer.cloudmall.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CommonCode implements IResponseCode {
    OK(HttpStatus.OK.value(), "操作成功"),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器发生未知异常"),
    SMS_CODE_TOO_MUCH_EXCEPTION(10002, "验证码获取频率太高，请稍后再试");

    private final int code;

    private final String message;
}
