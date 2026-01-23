package com.rainer.cloudmall.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import com.rainer.cloudmall.common.to.SpuBoundsTo;
import com.rainer.cloudmall.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.rainer.cloudmall.coupon.entity.SpuBoundsEntity;
import com.rainer.cloudmall.coupon.service.SpuBoundsService;
import com.rainer.cloudmall.common.utils.PageUtils;


/**
 * 商品spu积分设置
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 14:39:14
 */
@RestController
@RequestMapping("coupon/spubounds")
public class SpuBoundsController {
    private final SpuBoundsService spuBoundsService;

    public SpuBoundsController(SpuBoundsService spuBoundsService) {
        this.spuBoundsService = spuBoundsService;
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("coupon:spubounds:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = spuBoundsService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("coupon:spubounds:info")
    public Result info(@PathVariable("id") Long id){
		SpuBoundsEntity spuBounds = spuBoundsService.getById(id);

        return Result.ok().put("spuBounds", spuBounds);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public Result save(@RequestBody SpuBoundsTo spuBoundsTo){
		spuBoundsService.save(spuBoundsTo);
        return Result.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("coupon:spubounds:update")
    public Result update(@RequestBody SpuBoundsEntity spuBounds){
		spuBoundsService.updateById(spuBounds);

        return Result.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("coupon:spubounds:delete")
    public Result delete(@RequestBody Long[] ids){
		spuBoundsService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
