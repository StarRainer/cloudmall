package com.rainer.cloudmall.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CommonCode implements IResponseCode {
    OK(HttpStatus.OK.value(), "操作成功");

    private final int code;

    private final String message;
}
