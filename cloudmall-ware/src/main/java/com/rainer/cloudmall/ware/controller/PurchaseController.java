package com.rainer.cloudmall.ware.controller;

import java.util.Arrays;
import java.util.Map;

import com.rainer.cloudmall.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.rainer.cloudmall.ware.entity.PurchaseEntity;
import com.rainer.cloudmall.ware.service.PurchaseService;
import com.rainer.cloudmall.common.utils.PageUtils;


/**
 * 采购信息
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 14:41:44
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping("/unreceive/list")
    public Result listUnreceivePruchase(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryUnreceivePurchasePage(params);

        return Result.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("ware:purchase:info")
    public Result info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return Result.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("ware:purchase:save")
    public Result save(@RequestBody PurchaseEntity purchase){
		purchaseService.save(purchase);

        return Result.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("ware:purchase:update")
    public Result update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return Result.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("ware:purchase:delete")
    public Result delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
