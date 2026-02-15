package com.rainer.cloudmall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.rainer.cloudmall.common.utils.FeignResult;
import com.rainer.cloudmall.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.rainer.cloudmall.product.entity.SkuSaleAttrValueEntity;
import com.rainer.cloudmall.product.service.SkuSaleAttrValueService;
import com.rainer.cloudmall.common.utils.PageUtils;


/**
 * sku销售属性&值
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 13:41:45
 */
@RestController
@RequestMapping("product/skusaleattrvalue")
public class SkuSaleAttrValueController {
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @GetMapping("/skuSaleAttrValues/{skuId}")
    public FeignResult<List<String>> getSkuSaleAttrValues(@PathVariable("skuId") Long skuId) {
        return FeignResult.success(skuSaleAttrValueService.getSkuSaleAttrValues(skuId));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("product:skusaleattrvalue:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = skuSaleAttrValueService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("product:skusaleattrvalue:info")
    public Result info(@PathVariable("id") Long id){
		SkuSaleAttrValueEntity skuSaleAttrValue = skuSaleAttrValueService.getById(id);

        return Result.ok().put("skuSaleAttrValue", skuSaleAttrValue);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("product:skusaleattrvalue:save")
    public Result save(@RequestBody SkuSaleAttrValueEntity skuSaleAttrValue){
		skuSaleAttrValueService.save(skuSaleAttrValue);

        return Result.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("product:skusaleattrvalue:update")
    public Result update(@RequestBody SkuSaleAttrValueEntity skuSaleAttrValue){
		skuSaleAttrValueService.updateById(skuSaleAttrValue);

        return Result.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("product:skusaleattrvalue:delete")
    public Result delete(@RequestBody Long[] ids){
		skuSaleAttrValueService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
