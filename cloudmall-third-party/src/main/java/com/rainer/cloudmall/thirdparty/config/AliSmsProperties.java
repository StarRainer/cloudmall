package com.rainer.cloudmall.thirdparty.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "alicloud.sms")
public class AliSmsProperties {
    private String url;
    private String appCode;
    private Integer expire;
    private String templateId;
}
