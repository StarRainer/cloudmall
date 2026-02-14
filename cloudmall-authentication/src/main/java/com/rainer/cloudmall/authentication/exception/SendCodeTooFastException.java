package com.rainer.cloudmall.authentication.exception;

import com.rainer.cloudmall.common.exception.BaseException;
import com.rainer.cloudmall.common.exception.code.CommonCode;

public class SendCodeTooFastException extends BaseException {
    public SendCodeTooFastException() {
        super(CommonCode.SMS_CODE_TOO_MUCH_EXCEPTION.getCode(), CommonCode.SMS_CODE_TOO_MUCH_EXCEPTION.getMessage());
    }

    public SendCodeTooFastException(String message) {
        super(CommonCode.SEND_CODE_FAILURE.getCode(), message);
    }
}
