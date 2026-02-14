package com.rainer.cloudmall.authentication.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GitHubConfigurationProperties.class)
public class GitHubConfiguration {
}
