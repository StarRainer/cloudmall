package com.rainer.cloudmail.order.dao;

import com.rainer.cloudmail.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 14:37:13
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
