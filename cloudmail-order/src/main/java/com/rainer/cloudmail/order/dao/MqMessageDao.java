package com.rainer.cloudmail.order.dao;

import com.rainer.cloudmail.order.entity.MqMessageEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 14:37:12
 */
@Mapper
public interface MqMessageDao extends BaseMapper<MqMessageEntity> {
	
}
