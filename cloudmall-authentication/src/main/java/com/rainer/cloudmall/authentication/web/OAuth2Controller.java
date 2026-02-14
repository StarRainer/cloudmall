package com.rainer.cloudmall.authentication.web;

import com.rainer.cloudmall.authentication.service.GithubOauthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OAuth2Controller {
    private final GithubOauthService githubOauthService;

    public OAuth2Controller(GithubOauthService githubOauthService) {
        this.githubOauthService = githubOauthService;
    }

    @GetMapping("/oauth2.0/github/success")
    public String githubSuccess(@RequestParam("code") String code, HttpSession session) {
        return githubOauthService.loginOrRegister(code, session);
    }
}
