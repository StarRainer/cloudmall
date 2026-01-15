package com.rainer.cloudmail.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import com.rainer.common.utils.Result;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rainer.cloudmail.coupon.entity.CouponEntity;
import com.rainer.cloudmail.coupon.service.CouponService;
import com.rainer.common.utils.PageUtils;


/**
 * 优惠券信息
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 14:39:15
 */
@RestController
@RequestMapping("coupon/coupon")
public class CouponController {
    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @RequestMapping("/member/info/{memberId}")
    public Result getInfoByMemberId(@PathVariable("memberId") Long memberId) {
        // TODO: 根据会员 ID 获取优惠券信息
        return Result.ok().put("coupons", memberId);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("coupon:coupon:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = couponService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("coupon:coupon:info")
    public Result info(@PathVariable("id") Long id){
		CouponEntity coupon = couponService.getById(id);

        return Result.ok().put("coupon", coupon);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("coupon:coupon:save")
    public Result save(@RequestBody CouponEntity coupon){
		couponService.save(coupon);

        return Result.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("coupon:coupon:update")
    public Result update(@RequestBody CouponEntity coupon){
		couponService.updateById(coupon);

        return Result.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("coupon:coupon:delete")
    public Result delete(@RequestBody Long[] ids){
		couponService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
