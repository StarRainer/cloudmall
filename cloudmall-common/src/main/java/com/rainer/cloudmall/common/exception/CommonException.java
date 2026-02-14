package com.rainer.cloudmall.common.exception;

import com.rainer.cloudmall.common.exception.code.CommonCode;

public class CommonException extends BaseException {
    public CommonException() {
        super(CommonCode.UNKNOWN_ERROR.getCode(), CommonCode.UNKNOWN_ERROR.getMessage());
    }

    public CommonException(String message) {
        super(CommonCode.UNKNOWN_ERROR.getCode(), message);
    }
}
