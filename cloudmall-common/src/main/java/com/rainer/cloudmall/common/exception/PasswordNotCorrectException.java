package com.rainer.cloudmall.common.exception;

import com.rainer.cloudmall.common.exception.code.UserCode;

public class PasswordNotCorrectException extends BaseException {
    public PasswordNotCorrectException() {
        super(UserCode.PASSWORD_NOT_CORRECT_EXCEPTION.getCode(), UserCode.PASSWORD_NOT_CORRECT_EXCEPTION.getMessage());
    }
}
