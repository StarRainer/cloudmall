package com.rainer.cloudmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainer.cloudmall.common.constant.ProductConstant;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Query;
import com.rainer.cloudmall.product.dao.AttrAttrgroupRelationDao;
import com.rainer.cloudmall.product.dao.AttrDao;
import com.rainer.cloudmall.product.dao.AttrGroupDao;
import com.rainer.cloudmall.product.entity.AttrAttrgroupRelationEntity;
import com.rainer.cloudmall.product.entity.AttrEntity;
import com.rainer.cloudmall.product.entity.AttrGroupEntity;
import com.rainer.cloudmall.product.utils.ProductMapper;
import com.rainer.cloudmall.product.service.AttrAttrgroupRelationService;
import com.rainer.cloudmall.product.vo.AttrGroupRelationVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@Service("attrAttrgroupRelationService")
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    private final AttrDao attrDao;

    private final AttrGroupDao attrGroupDao;

    private final AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    private final ProductMapper productMapper;

    public AttrAttrgroupRelationServiceImpl(
            AttrDao attrDao,
            AttrGroupDao attrGroupDao,
            AttrAttrgroupRelationDao attrAttrgroupRelationDao,
            ProductMapper productMapper
    ) {
        this.attrDao = attrDao;
        this.attrGroupDao = attrGroupDao;
        this.attrAttrgroupRelationDao = attrAttrgroupRelationDao;
        this.productMapper = productMapper;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<AttrEntity> listAttrByAttrGroupIds(List<Long> attrIds) {
        return CollectionUtils.isEmpty(attrIds) ? Collections.emptyList() : attrDao.selectByIds(attrIds);
    }

    @Override
    public AttrGroupEntity getAttrGroupByAttrGroupId(Long attrGroupId) {
        return attrGroupDao.selectById(attrGroupId);
    }

    @Override
    public List<Long> getOccupiedAttrIds(List<Long> groupIds) {
        if (groupIds == null || groupIds.isEmpty()) {
            return Collections.emptyList();
        }
        return list(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                .select(AttrAttrgroupRelationEntity::getAttrId)
                .in(AttrAttrgroupRelationEntity::getAttrGroupId, groupIds)
        ).stream().map(AttrAttrgroupRelationEntity::getAttrId).toList();
    }

    @Override
    public PageUtils getSPUPageExcludeByAttrId(Map<String, Object> params, Long catelogId, List<Long> attrIds) {
        String key = (String) params.get("key");
        boolean isNumber = key != null && key.matches("^\\d+$");
        return new PageUtils(attrDao.selectPage(
                new Query<AttrEntity>().getPage(params),
                new LambdaQueryWrapper<AttrEntity>()
                        .eq(AttrEntity::getCatelogId, catelogId)
                        .eq(AttrEntity::getAttrType, ProductConstant.AttrType.BASE.getCode())
                        .notIn(!CollectionUtils.isEmpty(attrIds), AttrEntity::getAttrId, attrIds)
                        .and(key != null, wrapper ->
                                wrapper.eq(isNumber, AttrEntity::getAttrId, isNumber ? Long.parseLong(key) : null)
                                        .or().like(AttrEntity::getAttrName, key)
                        )
        ));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRelationWithAttr(List<AttrGroupRelationVo> attrGroupRelationVo) {
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = attrGroupRelationVo.stream()
                .map(productMapper::attrGroupRelationVoToAttrAttrgroupRelationEntity)
                .toList();
        attrAttrgroupRelationDao.deleteBatchRelation(attrAttrgroupRelationEntities);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(List<AttrGroupRelationVo> attrGroupRelationVos) {
        saveBatch(attrGroupRelationVos.stream()
                .map(productMapper::attrGroupRelationVoToAttrAttrgroupRelationEntity)
                .toList()
        );
    }

    @Override
    public List<AttrAttrgroupRelationEntity> getAttrAttrGroupRelationsByAttrGroupIds(List<Long> attrGroupIds) {
        if (CollectionUtils.isEmpty(attrGroupIds)) {
            return Collections.emptyList();
        }
        return list(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                .in(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupIds)
        );
    }

    @Override
    public List<AttrEntity> getAttrsByAttrIds(List<Long> attrIds) {
        if (CollectionUtils.isEmpty(attrIds)) {
            return Collections.emptyList();
        }
        return attrDao.selectByIds(attrIds);
    }

}