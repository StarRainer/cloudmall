package com.rainer.cloudmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainer.cloudmall.product.dao.CategoryBrandRelationDao;
import com.rainer.cloudmall.product.entity.CategoryBrandRelationEntity;
import com.rainer.cloudmall.product.service.CategoryBrandRelationService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Override
    public List<CategoryBrandRelationEntity> listCateLog(Long brandId) {
        return list(new LambdaQueryWrapper<CategoryBrandRelationEntity>()
                .eq(CategoryBrandRelationEntity::getBrandId, brandId));
    }
}