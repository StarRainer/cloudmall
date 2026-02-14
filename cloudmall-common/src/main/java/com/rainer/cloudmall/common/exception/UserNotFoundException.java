package com.rainer.cloudmall.common.exception;

import com.rainer.cloudmall.common.exception.code.UserCode;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException() {
        super(UserCode.USER_NOT_FOUND_EXCEPTION.getCode(), UserCode.USER_NOT_FOUND_EXCEPTION.getMessage());
    }
}
