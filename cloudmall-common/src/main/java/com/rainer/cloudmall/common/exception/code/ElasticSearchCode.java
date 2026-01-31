package com.rainer.cloudmall.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ElasticSearchCode {
    QUERY_EXCEPTION(900, "ElasticSearch 查询失败");

    private final int code;

    private final String message;

}
