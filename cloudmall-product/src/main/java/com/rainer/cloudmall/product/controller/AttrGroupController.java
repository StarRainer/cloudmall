package com.rainer.cloudmall.product.controller;

import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Result;
import com.rainer.cloudmall.product.entity.AttrGroupEntity;
import com.rainer.cloudmall.product.service.AttrGroupService;
import com.rainer.cloudmall.product.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 属性分组
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 13:41:45
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    private final AttrGroupService attrGroupService;

    private final CategoryService categoryService;

    public AttrGroupController(AttrGroupService attrGroupService, CategoryService categoryService) {
        this.attrGroupService = attrGroupService;
        this.categoryService = categoryService;
    }

    /**
     * 列表
     */
    @GetMapping("/list/{catelogId}")
    public Result list(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId) {
        return Result.ok().put("page", attrGroupService.queryPage(params, catelogId));
    }


    /**
     * 信息
     */
    @GetMapping("/info/{attrGroupId}")
    public Result info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        attrGroup.setCatelogPath(categoryService.getPathLink(attrGroup.getCatelogId()));
        return Result.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public Result save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return Result.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return Result.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public Result delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return Result.ok();
    }

}
