package com.rainer.cloudmall.thirdparty.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SmsResponseVo {
    private String status;
    private String reason;
    @JsonProperty("request_id")
    private String requestId;
}
