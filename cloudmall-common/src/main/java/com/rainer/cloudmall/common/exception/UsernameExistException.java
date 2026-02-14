package com.rainer.cloudmall.common.exception;

import com.rainer.cloudmall.common.exception.code.UserCode;

public class UsernameExistException extends BaseException {
    public UsernameExistException() {
        super(UserCode.USERNAME_EXIST_EXCEPTION.getCode(), UserCode.USERNAME_EXIST_EXCEPTION.getMessage());
    }
}
