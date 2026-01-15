package com.rainer.cloudmail.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.rainer.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rainer.cloudmail.product.entity.SpuCommentEntity;
import com.rainer.cloudmail.product.service.SpuCommentService;
import com.rainer.common.utils.PageUtils;


/**
 * 商品评价
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 13:41:45
 */
@RestController
@RequestMapping("product/spucomment")
public class SpuCommentController {
    @Autowired
    private SpuCommentService spuCommentService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("product:spucomment:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = spuCommentService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("product:spucomment:info")
    public Result info(@PathVariable("id") Long id){
		SpuCommentEntity spuComment = spuCommentService.getById(id);

        return Result.ok().put("spuComment", spuComment);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("product:spucomment:save")
    public Result save(@RequestBody SpuCommentEntity spuComment){
		spuCommentService.save(spuComment);

        return Result.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("product:spucomment:update")
    public Result update(@RequestBody SpuCommentEntity spuComment){
		spuCommentService.updateById(spuComment);

        return Result.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("product:spucomment:delete")
    public Result delete(@RequestBody Long[] ids){
		spuCommentService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
