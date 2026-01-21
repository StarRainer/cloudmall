package com.rainer.cloudmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.product.entity.AttrAttrgroupRelationEntity;
import com.rainer.cloudmall.product.entity.AttrEntity;
import com.rainer.cloudmall.product.entity.AttrGroupEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 11:20:39
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<AttrEntity> listAttrByAttrGroupIds(List<Long> attrIds);

    AttrGroupEntity getAttrGroupByAttrGroupId(Long attrGroupId);

    void removeByAttrIdsAndAttrGroupIds(List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities);

    List<Long> getOccupiedAttrIds(List<Long> groupIds);

    PageUtils getSPUPageExcludeByAttrId(Map<String, Object> params, Long catelogId, List<Long> attrIds);
}

