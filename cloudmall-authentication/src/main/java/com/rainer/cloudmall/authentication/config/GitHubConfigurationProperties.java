package com.rainer.cloudmall.authentication.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cloudmall.github")
public class GitHubConfigurationProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
