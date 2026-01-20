package com.rainer.cloudmall.product.controller;

import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Result;
import com.rainer.cloudmall.product.entity.AttrEntity;
import com.rainer.cloudmall.product.service.AttrService;
import com.rainer.cloudmall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 商品属性
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 13:41:45
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    private final AttrService attrService;

    public AttrController(AttrService attrService) {
        this.attrService = attrService;
    }

    /**
     * 列表
     */
    @GetMapping("/base/list/{attrId}")
    public Result list(@RequestParam Map<String, Object> params, @PathVariable("attrId") Long attrId) {
        PageUtils page = attrService.queryPage(params, attrId);
        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
//    @RequiresPermissions("product:attr:info")
    public Result info(@PathVariable("attrId") Long attrId){
		AttrEntity attr = attrService.getById(attrId);

        return Result.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody AttrVo attrVo){
		attrService.saveAttr(attrVo);
        return Result.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("product:attr:update")
    public Result update(@RequestBody AttrEntity attr){
		attrService.updateById(attr);

        return Result.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("product:attr:delete")
    public Result delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return Result.ok();
    }

}
