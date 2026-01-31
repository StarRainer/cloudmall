package com.rainer.cloudmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Query;
import com.rainer.cloudmall.product.dao.BrandDao;
import com.rainer.cloudmall.product.entity.BrandEntity;
import com.rainer.cloudmall.product.service.BrandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@Service("brandService")
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {
    private final CategoryBrandRelationServiceImpl categoryBrandRelationService;

    public BrandServiceImpl(CategoryBrandRelationServiceImpl categoryBrandRelationService) {
        this.categoryBrandRelationService = categoryBrandRelationService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        boolean isNumber = key != null && key.matches("^\\d+$");

        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                new LambdaQueryWrapper<BrandEntity>()
                        .eq(isNumber, BrandEntity::getBrandId, isNumber ? Long.parseLong(key) : key)
                        .or()
                        .like(key != null, BrandEntity::getName, key)
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCascade(BrandEntity brand) {
        updateById(brand);
        if (StringUtils.hasLength(brand.getName())) {
            categoryBrandRelationService.updateBrand(brand.getBrandId(), brand.getName());

            // TODO: 更新其他关联
        }
    }

    @Override
    public List<BrandEntity> listNameAndLogoByIds(List<Long> brandIds) {
        if (CollectionUtils.isEmpty(brandIds)) {
            return Collections.emptyList();
        }
        return list(new LambdaQueryWrapper<BrandEntity>()
                .select(BrandEntity::getBrandId, BrandEntity::getName, BrandEntity::getLogo)
                .in(BrandEntity::getBrandId, brandIds)
        );
    }

}