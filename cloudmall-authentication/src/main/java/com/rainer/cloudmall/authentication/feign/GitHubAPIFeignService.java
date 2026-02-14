package com.rainer.cloudmall.authentication.feign;

import com.rainer.cloudmall.authentication.to.GitHubUserTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "github-api-service", url = "https://api.github.com")
public interface GitHubAPIFeignService {
    @GetMapping("/user")
    GitHubUserTo getGitHubUser(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String acceptType
    );
}
