package com.rainer.cloudmall.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CommonCode implements IResponseCode {
    OK(HttpStatus.OK.value(), "操作成功"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "请求格式错误"),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器发生未知异常"),
    SMS_CODE_TOO_MUCH_EXCEPTION(10002, "验证码获取频率太高，请稍后再试"),
    SEND_CODE_FAILURE(10003, "发送验证码失败");

    private final int code;

    private final String message;
}
