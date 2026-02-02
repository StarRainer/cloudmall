package com.rainer.cloudmall.authentication.feign;

import com.rainer.cloudmall.authentication.vo.UserRegisterVo;
import com.rainer.cloudmall.common.utils.FeignResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "cloudmall-member")
public interface MemberFeignService {
    @PostMapping("/member/member/register")
    FeignResult<Void> register(@RequestBody UserRegisterVo memberRegisterVo);
}
