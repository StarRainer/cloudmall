package com.rainer.cloudmall.coupon.controller;

import com.rainer.cloudmall.common.to.SkuReductionTo;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Result;
import com.rainer.cloudmall.coupon.entity.SkuFullReductionEntity;
import com.rainer.cloudmall.coupon.service.SkuFullReductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * 商品满减信息
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 14:39:14
 */
@RestController
@RequestMapping("coupon/skufullreduction")
public class SkuFullReductionController {
    private final SkuFullReductionService skuFullReductionService;

    public SkuFullReductionController(SkuFullReductionService skuFullReductionService) {
        this.skuFullReductionService = skuFullReductionService;
    }

    @PostMapping("/save/batch")
    Result saveSkuReductions(@RequestBody List<SkuReductionTo> skuReductionTos) {
        skuFullReductionService.saveSkuReductions(skuReductionTos);
        return Result.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("coupon:skufullreduction:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = skuFullReductionService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("coupon:skufullreduction:info")
    public Result info(@PathVariable("id") Long id){
		SkuFullReductionEntity skuFullReduction = skuFullReductionService.getById(id);

        return Result.ok().put("skuFullReduction", skuFullReduction);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("coupon:skufullreduction:save")
    public Result save(@RequestBody SkuFullReductionEntity skuFullReduction){
		skuFullReductionService.save(skuFullReduction);

        return Result.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("coupon:skufullreduction:update")
    public Result update(@RequestBody SkuFullReductionEntity skuFullReduction){
		skuFullReductionService.updateById(skuFullReduction);

        return Result.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("coupon:skufullreduction:delete")
    public Result delete(@RequestBody Long[] ids){
		skuFullReductionService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
