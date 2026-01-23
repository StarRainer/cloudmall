package com.rainer.cloudmall.product.controller;

import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Result;
import com.rainer.cloudmall.product.entity.SpuInfoEntity;
import com.rainer.cloudmall.product.service.SpuInfoService;
import com.rainer.cloudmall.product.vo.SpuSaveVo;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * spu信息
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 13:41:45
 */
@RestController
@RequestMapping("product/spuinfo")
public class SpuInfoController {
    private final SpuInfoService spuInfoService;


    public SpuInfoController(SpuInfoService spuInfoService) {
        this.spuInfoService = spuInfoService;
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = spuInfoService.queryPage(params);
        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("product:spuinfo:info")
    public Result info(@PathVariable("id") Long id){
		SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return Result.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody SpuSaveVo spuSaveVo){
		spuInfoService.save(spuSaveVo);
        return Result.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("product:spuinfo:update")
    public Result update(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.updateById(spuInfo);

        return Result.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("product:spuinfo:delete")
    public Result delete(@RequestBody Long[] ids){
		spuInfoService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
