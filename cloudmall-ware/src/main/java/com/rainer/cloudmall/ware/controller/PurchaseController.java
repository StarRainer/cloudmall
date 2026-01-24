package com.rainer.cloudmall.ware.controller;

import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Result;
import com.rainer.cloudmall.ware.entity.PurchaseEntity;
import com.rainer.cloudmall.ware.service.PurchaseService;
import com.rainer.cloudmall.ware.vo.FinishPurchaseVo;
import com.rainer.cloudmall.ware.vo.MergePurchaseVo;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


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

    @PostMapping("/merge")
    public Result mergePurchase(@RequestBody MergePurchaseVo mergePurchaseVo) {
        purchaseService.mergePurchase(mergePurchaseVo);
        return Result.ok();
    }

    @PostMapping("/receive")
    public Result receivePurchase(@RequestBody List<Long> purchaseIds) {
        purchaseService.receivePurchase(purchaseIds);
        return Result.ok();
    }

    @PostMapping("/done")
    public Result donePurchase(@RequestBody FinishPurchaseVo finishPurchaseVo) {
        purchaseService.finishPurchase(finishPurchaseVo);
        return Result.ok();
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
