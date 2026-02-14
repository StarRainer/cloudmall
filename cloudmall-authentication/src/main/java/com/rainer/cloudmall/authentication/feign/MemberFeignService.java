package com.rainer.cloudmall.authentication.feign;

import com.rainer.cloudmall.authentication.to.GitHubUserTo;
import com.rainer.cloudmall.authentication.vo.UserLoginVo;
import com.rainer.cloudmall.authentication.vo.UserRegisterVo;
import com.rainer.cloudmall.authentication.vo.UserVo;
import com.rainer.cloudmall.common.utils.FeignResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "cloudmall-member")
public interface MemberFeignService {
    @PostMapping("/member/member/register")
    FeignResult<Void> register(@RequestBody UserRegisterVo memberRegisterVo);

    @PostMapping("/member/member/login")
    FeignResult<UserVo> login(@RequestBody UserLoginVo userLoginVo);

    @PostMapping("/member/member/github/login")
    FeignResult<UserVo> login(@RequestBody GitHubUserTo gitHubUserTo);
}
