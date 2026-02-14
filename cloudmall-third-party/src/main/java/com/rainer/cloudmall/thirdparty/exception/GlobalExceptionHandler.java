package com.rainer.cloudmall.thirdparty.exception;

import com.rainer.cloudmall.common.exception.code.ProductResponseCode;
import com.rainer.cloudmall.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("数据校验失败{}", e.getMessage(), e);
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errorMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach(fieldError -> errorMap.put(fieldError.getField(), fieldError.getDefaultMessage()));
        return Result.error(
                ProductResponseCode.VALID_EXCEPTION.getCode(),
                ProductResponseCode.VALID_EXCEPTION.getMessage()
        ).put("data", errorMap);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public Result handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("请求数据解析失败{}", e.getMessage(), e);
        return Result.error(
                ProductResponseCode.REQUEST_PARSE_EXCEPTION.getCode(),
                ProductResponseCode.REQUEST_PARSE_EXCEPTION.getMessage()
        );
    }

    @ExceptionHandler(value = HttpClientErrorException.class)
    public Result handleHttpClientErrorException(HttpClientErrorException e) {
        String xCaError = null;
        if (e.getResponseHeaders() != null) {
            xCaError = e.getResponseHeaders().getFirst("X-Ca-Error-Message");
        }
        String errorBody = e.getResponseBodyAsString();

        log.error("调用失败: Status={}, X-Ca-Error-Message={}, Body={}",
                e.getStatusCode(),
                xCaError,
                errorBody
        );
        return Result.error("第三方服务调用失败");
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("系统出现未知异常{}", e.getMessage(), e);
        return Result.error(
                ProductResponseCode.UNKNOWN_EXCEPTION.getCode(),
                ProductResponseCode.UNKNOWN_EXCEPTION.getMessage()
        );
    }
}
