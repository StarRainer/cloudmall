package com.rainer.cloudmall.product.controller;

import com.rainer.cloudmall.common.utils.Result;
import com.rainer.cloudmall.product.entity.CategoryBrandRelationEntity;
import com.rainer.cloudmall.product.service.CategoryBrandRelationService;
import com.rainer.cloudmall.product.vo.BrandResVo;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * 品牌分类关联
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 13:41:45
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {

    private final CategoryBrandRelationService categoryBrandRelationService;

    public CategoryBrandRelationController(CategoryBrandRelationService categoryBrandRelationService) {
        this.categoryBrandRelationService = categoryBrandRelationService;
    }

    /**
     * 列表
     */
    @GetMapping("/catelog/list")
    public Result list(@RequestParam("brandId") Long brandId) {
        return Result.ok().put("data", categoryBrandRelationService.listCateLog(brandId));
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return Result.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public Result save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.saveDetail(categoryBrandRelation);
        return Result.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return Result.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public Result delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

    /**
     * 获取分类下的所有品牌
     */
    @GetMapping("/brands/list")
    public Result listBrands(@RequestParam("catId") Long catId) {
        List<BrandResVo> brandResVos = categoryBrandRelationService.listBrandsByCatId(catId);
        return Result.ok().put("data", brandResVos);
    }
}
