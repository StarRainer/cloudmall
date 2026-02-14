package com.rainer.cloudmall.common.exception;

import com.rainer.cloudmall.common.exception.code.UserCode;

public class PhoneExistException extends BaseException {
    public PhoneExistException() {
        super(UserCode.PHONE_EXIST_EXCEPTION.getCode(), UserCode.PHONE_EXIST_EXCEPTION.getMessage());
    }
}
