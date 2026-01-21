package com.rainer.cloudmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Query;
import com.rainer.cloudmall.product.dao.AttrAttrgroupRelationDao;
import com.rainer.cloudmall.product.dao.AttrGroupDao;
import com.rainer.cloudmall.product.entity.AttrAttrgroupRelationEntity;
import com.rainer.cloudmall.product.entity.AttrEntity;
import com.rainer.cloudmall.product.entity.AttrGroupEntity;
import com.rainer.cloudmall.product.mapper.AttrMapper;
import com.rainer.cloudmall.product.service.AttrAttrgroupRelationService;
import com.rainer.cloudmall.product.service.AttrGroupService;
import com.rainer.cloudmall.product.vo.AttrGroupRelationVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;


@Service("attrGroupService")
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    private final AttrAttrgroupRelationService attrAttrgroupRelationService;

    private final AttrMapper attrMapper;
    private final AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    public AttrGroupServiceImpl(AttrAttrgroupRelationService attrAttrgroupRelationService, AttrMapper attrMapper, AttrAttrgroupRelationDao attrAttrgroupRelationDao) {
        this.attrAttrgroupRelationService = attrAttrgroupRelationService;
        this.attrMapper = attrMapper;
        this.attrAttrgroupRelationDao = attrAttrgroupRelationDao;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        // SELECT * FROM pms_attr_group WHERE catelog_id = #{catelogId} and (attr_group_id = #{key} OR attr_group_name LIKE '%#{key}%')
        String key = (String) params.get("key");
        boolean isNumber = key != null && key.matches("^\\d+$");
        return new PageUtils(page(
                new Query<AttrGroupEntity>().getPage(params),
                new LambdaQueryWrapper<AttrGroupEntity>()
                        .eq(catelogId != 0, AttrGroupEntity::getCatelogId, catelogId)
                        .and(StringUtils.hasText(key), object ->
                                object.eq(isNumber, AttrGroupEntity::getAttrGroupId, isNumber ? Long.parseLong(key) : null).or().like(AttrGroupEntity::getAttrGroupName, key)
                        )
        ));
    }

    @Override
    public List<AttrEntity> getAttr(Long attrGroupId) {
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = attrAttrgroupRelationService
                .list(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                        .eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupId)
                );
        List<Long> attrIds = attrAttrgroupRelationEntities.stream()
                .map(AttrAttrgroupRelationEntity::getAttrId)
                .toList();
        return attrAttrgroupRelationService.listAttrByAttrGroupIds(attrIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRelationWithAttr(List<AttrGroupRelationVo> attrGroupRelationVo) {
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = attrGroupRelationVo.stream()
                .map(attrMapper::attrGroupRelationVoToAttrAttrgroupRelationEntity)
                .toList();
        attrAttrgroupRelationService.removeByAttrIdsAndAttrGroupIds(attrAttrgroupRelationEntities);
    }

    @Override
    public PageUtils getAttrWithNoRelation(Map<String, Object> params, Long attrGroupId) {
        AttrGroupEntity attrGroupEntity = getById(attrGroupId);
        // 目标属性 = 当前分类下的所有属性 - 已经被当前分类下的分组占用的属性
        // 必须是 SPU 属性
        Long catelogId = attrGroupEntity.getCatelogId();

        // 1. 查询当前分类下的groupIds
        List<Long> groupIds = list(new LambdaQueryWrapper<AttrGroupEntity>()
                .select(AttrGroupEntity::getAttrGroupId)
                .eq(AttrGroupEntity::getCatelogId, catelogId)
        ).stream().map(AttrGroupEntity::getAttrGroupId).toList();

        // 2. 查询groupIds占用的attrIds
        List<Long> attrIds = attrAttrgroupRelationService.getOccupiedAttrIds(groupIds);

        // 3. 查询不在attrIds的 SPU 属性
        return attrAttrgroupRelationService.getSPUPageExcludeByAttrId(params, catelogId, attrIds);
    }
}