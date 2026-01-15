package com.rainer.cloudmail.order.controller;

import java.util.Arrays;
import java.util.Map;

import com.rainer.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rainer.cloudmail.order.entity.OrderItemEntity;
import com.rainer.cloudmail.order.service.OrderItemService;
import com.rainer.common.utils.PageUtils;


/**
 * 订单项信息
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 14:37:13
 */
@RestController
@RequestMapping("order/orderitem")
public class OrderItemController {
    @Autowired
    private OrderItemService orderItemService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("order:orderitem:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = orderItemService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("order:orderitem:info")
    public Result info(@PathVariable("id") Long id){
		OrderItemEntity orderItem = orderItemService.getById(id);

        return Result.ok().put("orderItem", orderItem);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("order:orderitem:save")
    public Result save(@RequestBody OrderItemEntity orderItem){
		orderItemService.save(orderItem);

        return Result.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("order:orderitem:update")
    public Result update(@RequestBody OrderItemEntity orderItem){
		orderItemService.updateById(orderItem);

        return Result.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("order:orderitem:delete")
    public Result delete(@RequestBody Long[] ids){
		orderItemService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
