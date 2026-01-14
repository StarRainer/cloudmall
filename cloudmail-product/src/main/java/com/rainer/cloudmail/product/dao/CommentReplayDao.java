package com.rainer.cloudmail.product.dao;

import com.rainer.cloudmail.product.entity.CommentReplayEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品评价回复关系
 * 
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 11:20:39
 */
@Mapper
public interface CommentReplayDao extends BaseMapper<CommentReplayEntity> {
	
}
