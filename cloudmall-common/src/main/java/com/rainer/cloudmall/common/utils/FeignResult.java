package com.rainer.cloudmall.common.utils;

import com.rainer.cloudmall.common.exception.code.CommonCode;
import com.rainer.cloudmall.common.exception.code.IResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeignResult<T> {
    public static <T> FeignResult<T> success() {
        return success(null);
    }

    public static <T> FeignResult<T> success(T data) {
        return new FeignResult<>(CommonCode.OK.getCode(), null, data);
    }

    public static <T> FeignResult<T> failure(IResponseCode code) {
        return new FeignResult<>(code.getCode(), code.getMessage(), null);
    }

    private int code;

    private String msg;

    private T data;
}
