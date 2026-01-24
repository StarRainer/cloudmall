package com.rainer.cloudmall.product.controller;

import com.rainer.cloudmall.common.utils.FeignResult;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Result;
import com.rainer.cloudmall.product.entity.SkuInfoEntity;
import com.rainer.cloudmall.product.service.SkuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * sku信息
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 13:41:45
 */
@RestController
@RequestMapping("product/skuinfo")
public class SkuInfoController {
    private final SkuInfoService skuInfoService;

    public SkuInfoController(SkuInfoService skuInfoService) {
        this.skuInfoService = skuInfoService;
    }

    @PostMapping("/skuname")
    FeignResult<Map<Long, String>> getSkuNamesBySkuIds(@RequestBody List<Long> skuIds) {
        return FeignResult.success(skuInfoService.getSkuNamesBySkuIds(skuIds));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = skuInfoService.queryPage(params);
        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{skuId}")
//    @RequiresPermissions("product:skuinfo:info")
    public Result info(@PathVariable("skuId") Long skuId){
		SkuInfoEntity skuInfo = skuInfoService.getById(skuId);

        return Result.ok().put("skuInfo", skuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("product:skuinfo:save")
    public Result save(@RequestBody SkuInfoEntity skuInfo){
		skuInfoService.save(skuInfo);

        return Result.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("product:skuinfo:update")
    public Result update(@RequestBody SkuInfoEntity skuInfo){
		skuInfoService.updateById(skuInfo);

        return Result.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("product:skuinfo:delete")
    public Result delete(@RequestBody Long[] skuIds){
		skuInfoService.removeByIds(Arrays.asList(skuIds));

        return Result.ok();
    }

}
