package com.rainer.cloudmail.member.feign;

import com.rainer.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "cloudmail-coupon", path = "/coupon/coupon")
public interface CouponFeignService {
    @RequestMapping("/member/info/{memberId}")
    Result getInfoByMemberId(@PathVariable("memberId") Long memberId);
}
