package com.rainer.cloudmall.product.controller;

import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Result;
import com.rainer.cloudmall.product.entity.AttrEntity;
import com.rainer.cloudmall.product.service.AttrService;
import com.rainer.cloudmall.product.vo.AttrResVo;
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
    @GetMapping("/base/list/{catelogId}")
    public Result list(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId) {
        PageUtils page = attrService.queryPage(params, catelogId);
        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{attrId}")
    public Result info(@PathVariable("attrId") Long attrId){
		AttrResVo attr = attrService.getAttrInfo(attrId);

        return Result.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public Result save(@RequestBody AttrVo attrVo){
		attrService.saveAttr(attrVo);
        return Result.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody AttrVo attr){
		attrService.updateById(attr);

        return Result.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public Result delete(@RequestBody Long[] attrIds){
		attrService.deleteAttr(Arrays.asList(attrIds));

        return Result.ok();
    }

}
