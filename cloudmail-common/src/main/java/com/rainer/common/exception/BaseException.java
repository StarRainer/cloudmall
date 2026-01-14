package com.rainer.common.exception;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {
    private final int code;

    private final String msg;

    public BaseException(String msg) {
        super(msg);
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.msg = msg;
    }

    public BaseException(String msg, Throwable e) {
        super(msg, e);
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.msg = msg;
    }

    public BaseException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
