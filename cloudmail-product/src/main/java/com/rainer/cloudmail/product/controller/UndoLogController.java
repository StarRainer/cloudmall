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

import com.rainer.cloudmail.product.entity.UndoLogEntity;
import com.rainer.cloudmail.product.service.UndoLogService;
import com.rainer.common.utils.PageUtils;


/**
 * 
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 13:41:45
 */
@RestController
@RequestMapping("product/undolog")
public class UndoLogController {
    @Autowired
    private UndoLogService undoLogService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("product:undolog:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = undoLogService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("product:undolog:info")
    public Result info(@PathVariable("id") Long id){
		UndoLogEntity undoLog = undoLogService.getById(id);

        return Result.ok().put("undoLog", undoLog);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("product:undolog:save")
    public Result save(@RequestBody UndoLogEntity undoLog){
		undoLogService.save(undoLog);

        return Result.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("product:undolog:update")
    public Result update(@RequestBody UndoLogEntity undoLog){
		undoLogService.updateById(undoLog);

        return Result.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("product:undolog:delete")
    public Result delete(@RequestBody Long[] ids){
		undoLogService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
