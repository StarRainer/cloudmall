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

import com.rainer.cloudmall.ware.entity.PurchaseDetailEntity;
import com.rainer.cloudmall.ware.service.PurchaseDetailService;
import com.rainer.cloudmall.common.utils.PageUtils;


/**
 * 
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 14:41:44
 */
@RestController
@RequestMapping("ware/purchasedetail")
public class PurchaseDetailController {
    private final PurchaseDetailService purchaseDetailService;

    public PurchaseDetailController(PurchaseDetailService purchaseDetailService) {
        this.purchaseDetailService = purchaseDetailService;
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseDetailService.queryPage(params);
        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("ware:purchasedetail:info")
    public Result info(@PathVariable("id") Long id){
		PurchaseDetailEntity purchaseDetail = purchaseDetailService.getById(id);

        return Result.ok().put("purchaseDetail", purchaseDetail);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("ware:purchasedetail:save")
    public Result save(@RequestBody PurchaseDetailEntity purchaseDetail){
		purchaseDetailService.save(purchaseDetail);

        return Result.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("ware:purchasedetail:update")
    public Result update(@RequestBody PurchaseDetailEntity purchaseDetail){
		purchaseDetailService.updateById(purchaseDetail);

        return Result.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("ware:purchasedetail:delete")
    public Result delete(@RequestBody Long[] ids){
		purchaseDetailService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
