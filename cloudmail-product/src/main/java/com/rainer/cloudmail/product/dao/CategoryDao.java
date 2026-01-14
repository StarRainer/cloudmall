package com.rainer.cloudmail.product.dao;

import com.rainer.cloudmail.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 11:20:39
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
