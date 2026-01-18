package com.rainer.cloudmail.product.controller;

import com.rainer.cloudmail.product.entity.CategoryEntity;
import com.rainer.cloudmail.product.service.CategoryService;
import com.rainer.common.utils.Result;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;



/**
 * 商品三级分类
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 13:41:45
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 查出所有分类及其子分类信息，以树形结构组装起来
     */
    @RequestMapping("/list/tree")
    public Result list(){
        List<CategoryEntity> categoryEntities = categoryService.listWithTree();
        return Result.ok().put("data", categoryEntities);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    public Result info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);
        return Result.ok().put("data", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody CategoryEntity category){
		categoryService.save(category);
        return Result.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("product:category:update")
    public Result update(@RequestBody CategoryEntity category){
		categoryService.updateById(category);
        return Result.ok();
    }

    /**
     * 批量修改
     */
    @RequestMapping("/update/batch")
    public Result updateBatch(@RequestBody CategoryEntity[] categoryEntities) {
        categoryService.updateBatchById(Arrays.asList(categoryEntities));
        return Result.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Long[] catIds){
		categoryService.removeMenusByIds(Arrays.asList(catIds));
        return Result.ok();
    }

}
