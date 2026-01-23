package com.rainer.cloudmall.ware.controller;

import java.util.Arrays;
import java.util.Map;

import com.rainer.cloudmall.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rainer.cloudmall.ware.entity.WareInfoEntity;
import com.rainer.cloudmall.ware.service.WareInfoService;
import com.rainer.cloudmall.common.utils.PageUtils;


/**
 * 仓库信息
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 14:41:44
 */
@RestController
@RequestMapping("ware/wareinfo")
public class WareInfoController {
    private final WareInfoService wareInfoService;

    public WareInfoController(WareInfoService wareInfoService) {
        this.wareInfoService = wareInfoService;
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = wareInfoService.queryPage(params);
        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("ware:wareinfo:info")
    public Result info(@PathVariable("id") Long id){
		WareInfoEntity wareInfo = wareInfoService.getById(id);

        return Result.ok().put("wareInfo", wareInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("ware:wareinfo:save")
    public Result save(@RequestBody WareInfoEntity wareInfo){
		wareInfoService.save(wareInfo);

        return Result.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("ware:wareinfo:update")
    public Result update(@RequestBody WareInfoEntity wareInfo){
		wareInfoService.updateById(wareInfo);

        return Result.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("ware:wareinfo:delete")
    public Result delete(@RequestBody Long[] ids){
		wareInfoService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
