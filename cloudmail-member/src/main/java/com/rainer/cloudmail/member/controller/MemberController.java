package com.rainer.cloudmail.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.rainer.cloudmail.member.feign.CouponFeignService;
import com.rainer.common.utils.Result;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rainer.cloudmail.member.entity.MemberEntity;
import com.rainer.cloudmail.member.service.MemberService;
import com.rainer.common.utils.PageUtils;


/**
 * 会员
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 14:40:48
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    private final MemberService memberService;

    private final CouponFeignService couponFeignService;

    public MemberController(MemberService memberService, CouponFeignService couponFeignService) {
        this.memberService = memberService;
        this.couponFeignService = couponFeignService;
    }

    @RequestMapping("/coupons/{memberId}")
    public Result getCouponInfo(@PathVariable("memberId") Long memberId) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("张三");
        memberEntity.setId(memberId);
        Result memberCoupons = couponFeignService.getInfoByMemberId(memberId);
        return Result.ok().put("member", memberEntity).put("coupons", memberCoupons.get("coupons"));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("member:member:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("member:member:info")
    public Result info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return Result.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("member:member:save")
    public Result save(@RequestBody MemberEntity member){
		memberService.save(member);

        return Result.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("member:member:update")
    public Result update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return Result.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("member:member:delete")
    public Result delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
