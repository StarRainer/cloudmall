package com.rainer.cloudmall.authentication.feign;

import com.rainer.cloudmall.authentication.to.GitHubAccessTokenTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "github-service", url = "https://github.com")
public interface GitHubFeignService {
    @PostMapping("/login/oauth/access_token")
    GitHubAccessTokenTo getAccessToken(
            @RequestHeader("Accept") String acceptType,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("code") String code,
            @RequestParam("redirect_uri") String redirectURI
    );

}
