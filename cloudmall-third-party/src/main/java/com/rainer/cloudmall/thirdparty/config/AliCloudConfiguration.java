package com.rainer.cloudmall.thirdparty.config;

import com.aliyun.sts20150401.Client;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AliOssProperties.class, AliSmsProperties.class})
public class AliCloudConfiguration {
    @Bean
    public Client client(AliOssProperties aliOssProperties) throws Exception {
        Config config = new Config()
                .setAccessKeyId(aliOssProperties.getAccessKeyId())
                .setAccessKeySecret(aliOssProperties.getAccessKeySecret())
                .setEndpoint(aliOssProperties.getEndpoint());
        return new Client(config);
    }
}
