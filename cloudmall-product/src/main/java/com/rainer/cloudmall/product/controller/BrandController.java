package com.rainer.cloudmall.product.controller;

import com.rainer.cloudmall.common.exception.valid.AddGroup;
import com.rainer.cloudmall.common.exception.valid.UpdateGroup;
import com.rainer.cloudmall.product.entity.BrandEntity;
import com.rainer.cloudmall.product.service.BrandService;
import com.rainer.cloudmall.common.utils.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 品牌
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 13:41:45
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    public Result list(@RequestParam Map<String, Object> params){
        return Result.ok().put("page", brandService.queryPage(params));
    }


    /**
     * 信息
     */
    @GetMapping("/info/{brandId}")
    public Result info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return Result.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public Result save(@Validated(AddGroup.class) @RequestBody BrandEntity brand){
		brandService.save(brand);

        return Result.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@Validated(UpdateGroup.class) @RequestBody BrandEntity brand){
		brandService.updateCascade(brand);
        return Result.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public Result delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return Result.ok();
    }

}
