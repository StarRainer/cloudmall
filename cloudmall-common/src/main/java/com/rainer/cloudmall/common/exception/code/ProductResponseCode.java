package com.rainer.cloudmall.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ProductResponseCode implements IResponseCode {
    UNKNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR.value(), "系统出现未知异常，请联系管理员"),
    VALID_EXCEPTION(HttpStatus.BAD_REQUEST.value(), "数据校验失败"),
    REQUEST_PARSE_EXCEPTION(700, "请求数据解析失败，请检查请求体格式");

    private final int code;

    private final String message;
}
