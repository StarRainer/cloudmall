package com.rainer.cloudmall.authentication.service;

import com.rainer.cloudmall.authentication.config.GitHubConfigurationProperties;
import com.rainer.cloudmall.authentication.feign.GitHubAPIFeignService;
import com.rainer.cloudmall.authentication.feign.GitHubFeignService;
import com.rainer.cloudmall.authentication.feign.MemberFeignService;
import com.rainer.cloudmall.authentication.to.GitHubUserTo;
import com.rainer.cloudmall.common.constant.SessionConstants;
import com.rainer.cloudmall.common.exception.code.CommonCode;
import com.rainer.cloudmall.common.utils.FeignResult;
import com.rainer.cloudmall.common.vo.UserVo;
import feign.FeignException;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GithubOauthService {
    private final GitHubFeignService gitHubFeignService;
    private final GitHubConfigurationProperties gitHubConfigurationProperties;
    private final MemberFeignService memberFeignService;
    private final GitHubAPIFeignService gitHubAPIFeignService;

    public GithubOauthService(GitHubFeignService gitHubFeignService, GitHubConfigurationProperties gitHubConfigurationProperties, MemberFeignService memberFeignService, GitHubAPIFeignService gitHubAPIFeignService) {
        this.gitHubFeignService = gitHubFeignService;
        this.gitHubConfigurationProperties = gitHubConfigurationProperties;
        this.memberFeignService = memberFeignService;
        this.gitHubAPIFeignService = gitHubAPIFeignService;
    }

    public String loginOrRegister(String code, HttpSession session) {
        // 根据code换取accessToken
        String accessToken = null;
        try {
            accessToken = gitHubFeignService.getAccessToken(
                    "application/json",
                    gitHubConfigurationProperties.getClientId(),
                    gitHubConfigurationProperties.getClientSecret(),
                    code,
                    gitHubConfigurationProperties.getRedirectUri()
            ).getAccessToken();
        } catch (FeignException e) {
            log.warn(e.getMessage());
            return "redirect:http://auth.cloudmall.com/login.html";
        }

        GitHubUserTo gitHubUserTo = null;
        try {
            gitHubUserTo = gitHubAPIFeignService.getGitHubUser(
                    "Bearer " + accessToken,
                    "application/vnd.github+json"
            );
        } catch (FeignException.Unauthorized e) {
            log.warn(e.getMessage());
            return "redirect:" + gitHubConfigurationProperties.getRedirectUri();
        } catch (FeignException e) {
            log.warn(e.getMessage());
            return "redirect:http://auth.cloudmall.com/login.html";
        }

        try {
            gitHubUserTo.setAccessToken(accessToken);
            FeignResult<UserVo> userVo = memberFeignService.login(gitHubUserTo);
            log.debug("userVo={}", userVo);
            if (userVo == null) {
                log.warn("远程调用第三方登录接口失败");
                return "redirect:http://auth.cloudmall.com/login.html";
            }
            if (userVo.getCode() == CommonCode.OK.getCode()) {
                session.setAttribute(SessionConstants.LOGIN_USER, userVo.getData());
            } else {
                log.warn(userVo.getMsg());
                return "redirect:http://auth.cloudmall.com/login.html";
            }
        } catch (FeignException e) {
            log.warn(e.getMessage());
            return "redirect:http://auth.cloudmall.com/login.html";
        }

        return "redirect:http://cloudmall.com";
    }
}
