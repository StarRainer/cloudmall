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

import com.rainer.cloudmail.order.entity.OrderEntity;
import com.rainer.cloudmail.order.service.OrderService;
import com.rainer.common.utils.PageUtils;


/**
 * 订单
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 14:37:12
 */
@RestController
@RequestMapping("order/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("order:order:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = orderService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("order:order:info")
    public Result info(@PathVariable("id") Long id){
		OrderEntity order = orderService.getById(id);

        return Result.ok().put("order", order);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("order:order:save")
    public Result save(@RequestBody OrderEntity order){
		orderService.save(order);

        return Result.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("order:order:update")
    public Result update(@RequestBody OrderEntity order){
		orderService.updateById(order);

        return Result.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("order:order:delete")
    public Result delete(@RequestBody Long[] ids){
		orderService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
