package com.rainer.cloudmall.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cloudmall.thread-pool")
public class ThreadPoolConfigurationProperties {
    private Integer corePoolSize;
    private Integer maximumPoolSize;
    private Long keepAliveTime;
}
