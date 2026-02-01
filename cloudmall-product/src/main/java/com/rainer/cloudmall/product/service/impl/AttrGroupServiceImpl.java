package com.rainer.cloudmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Query;
import com.rainer.cloudmall.product.dao.AttrGroupDao;
import com.rainer.cloudmall.product.entity.AttrAttrgroupRelationEntity;
import com.rainer.cloudmall.product.entity.AttrEntity;
import com.rainer.cloudmall.product.entity.AttrGroupEntity;
import com.rainer.cloudmall.product.service.AttrAttrgroupRelationService;
import com.rainer.cloudmall.product.service.AttrGroupService;
import com.rainer.cloudmall.product.utils.ProductMapper;
import com.rainer.cloudmall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service("attrGroupService")
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    private final AttrAttrgroupRelationService attrAttrgroupRelationService;
    private final ProductMapper productMapper;

    public AttrGroupServiceImpl(AttrAttrgroupRelationService attrAttrgroupRelationService, ProductMapper productMapper) {
        this.attrAttrgroupRelationService = attrAttrgroupRelationService;
        this.productMapper = productMapper;
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

    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupsWithAttrs(Long catelogId) {
        List<AttrGroupWithAttrsVo> attrGroupWithAttrsVos = list(new LambdaQueryWrapper<AttrGroupEntity>()
                .eq(AttrGroupEntity::getCatelogId, catelogId)
        ).stream().map(productMapper::attrGroupEntityToAttrGroupWithAttrsVo).toList();

        if (CollectionUtils.isEmpty(attrGroupWithAttrsVos)) {
            return attrGroupWithAttrsVos;
        }

        List<Long> attrGroupIds = attrGroupWithAttrsVos.stream().map(AttrGroupWithAttrsVo::getAttrGroupId).toList();
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = attrAttrgroupRelationService.getAttrAttrGroupRelationsByAttrGroupIds(attrGroupIds);

        if (CollectionUtils.isEmpty(attrAttrgroupRelationEntities)) {
            return attrGroupWithAttrsVos;
        }

        List<Long> attrIds = attrAttrgroupRelationEntities.stream().map(AttrAttrgroupRelationEntity::getAttrId).toList();
        List<AttrEntity> attrEntities = attrAttrgroupRelationService.getAttrsByAttrIds(attrIds);

        Map<Long, List<AttrAttrgroupRelationEntity>> attrGroupIdToAttrIds = attrAttrgroupRelationEntities.stream().collect(
                Collectors.groupingBy(AttrAttrgroupRelationEntity::getAttrGroupId)
        );
        Map<Long, AttrEntity> attrIdToAttrEntity = attrEntities.stream().collect(Collectors.toMap(AttrEntity::getAttrId, Function.identity()));

        attrGroupWithAttrsVos.forEach(attrGroupWithAttrsVo -> {
            List<AttrAttrgroupRelationEntity> currentAttrAttrGroupEntities = attrGroupIdToAttrIds.get(attrGroupWithAttrsVo.getAttrGroupId());
            if (CollectionUtils.isEmpty(currentAttrAttrGroupEntities)) {
                return;
            }
            List<AttrEntity> resultAttrs = currentAttrAttrGroupEntities.stream()
                    .map(attrattrGroupEntity -> attrIdToAttrEntity.get(attrattrGroupEntity.getAttrId()))
                    .filter(Objects::nonNull)
                    .toList();
            attrGroupWithAttrsVo.setAttrs(resultAttrs);
        });

        return attrGroupWithAttrsVos;
    }

    @Override
    public List<AttrGroupEntity> getAttrGroupByCatalogId(Long catalogId) {
        return list(new LambdaQueryWrapper<AttrGroupEntity>()
                .eq(AttrGroupEntity::getCatelogId, catalogId)
        );
    }
}