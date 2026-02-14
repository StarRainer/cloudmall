package com.rainer.cloudmall.authentication.exception;

import com.rainer.cloudmall.common.exception.BaseException;
import com.rainer.cloudmall.common.exception.code.CommonCode;

public class SendCodeFailureException extends BaseException {
    public SendCodeFailureException() {
        super(CommonCode.SEND_CODE_FAILURE.getCode(), CommonCode.SEND_CODE_FAILURE.getMessage());
    }
}
