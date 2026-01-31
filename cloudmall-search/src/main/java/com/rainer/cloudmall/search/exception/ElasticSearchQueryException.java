package com.rainer.cloudmall.search.exception;

import com.rainer.cloudmall.common.exception.BaseException;
import com.rainer.cloudmall.common.exception.code.ElasticSearchCode;

public class ElasticSearchQueryException extends BaseException {
    public ElasticSearchQueryException() {
        super(ElasticSearchCode.QUERY_EXCEPTION.getCode(), ElasticSearchCode.QUERY_EXCEPTION.getMessage());
    }
}
