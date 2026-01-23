package com.rainer.cloudmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainer.cloudmall.product.dao.BrandDao;
import com.rainer.cloudmall.product.dao.CategoryBrandRelationDao;
import com.rainer.cloudmall.product.dao.CategoryDao;
import com.rainer.cloudmall.product.entity.BrandEntity;
import com.rainer.cloudmall.product.entity.CategoryBrandRelationEntity;
import com.rainer.cloudmall.product.entity.CategoryEntity;
import com.rainer.cloudmall.product.service.CategoryBrandRelationService;
import com.rainer.cloudmall.product.utils.ProductMapper;
import com.rainer.cloudmall.product.vo.BrandResVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;


@Service("categoryBrandRelationService")
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    private final BrandDao brandDao;

    private final CategoryDao categoryDao;

    private final ProductMapper productMapper;

    public CategoryBrandRelationServiceImpl(BrandDao brandDao, CategoryDao categoryDao, ProductMapper productMapper) {
        this.brandDao = brandDao;
        this.categoryDao = categoryDao;
        this.productMapper = productMapper;
    }

    @Override
    public List<CategoryBrandRelationEntity> listCateLog(Long brandId) {
        return list(new LambdaQueryWrapper<CategoryBrandRelationEntity>()
                .eq(CategoryBrandRelationEntity::getBrandId, brandId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        BrandEntity brandEntity = brandDao.selectById(categoryBrandRelation.getBrandId());
        CategoryEntity categoryEntity = categoryDao.selectById(categoryBrandRelation.getCatelogId());
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        save(categoryBrandRelation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBrand(Long brandId, String name) {
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setBrandId(brandId);
        categoryBrandRelationEntity.setBrandName(name);
        update(categoryBrandRelationEntity,
                new LambdaUpdateWrapper<CategoryBrandRelationEntity>()
                        .eq(CategoryBrandRelationEntity::getBrandId, brandId)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(Long catId, String name) {
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setCatelogId(catId);
        categoryBrandRelationEntity.setCatelogName(name);
        update(categoryBrandRelationEntity,
                new LambdaUpdateWrapper<CategoryBrandRelationEntity>()
                        .eq(CategoryBrandRelationEntity::getCatelogId, catId)
        );
    }

    @Override
    public List<BrandResVo> listBrandsByCatId(Long catId) {
        List<Long> brandIds = list(new LambdaQueryWrapper<CategoryBrandRelationEntity>()
                .eq(CategoryBrandRelationEntity::getCatelogId, catId)
        ).stream().map(CategoryBrandRelationEntity::getBrandId).toList();
        if (CollectionUtils.isEmpty(brandIds)) {
            return Collections.emptyList();
        }
        return brandDao.selectByIds(brandIds).stream()
                .map(productMapper::brandEntityToBrandResVo).toList();
    }
}