package com.rainer.cloudmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.product.entity.AttrEntity;
import com.rainer.cloudmall.product.entity.AttrGroupEntity;
import com.rainer.cloudmall.product.vo.AttrGroupRelationVo;
import com.rainer.cloudmall.product.vo.AttrGroupWithAttrsVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 11:20:39
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Long catelogId);

    List<AttrEntity> getAttr(Long attrGroupId);

    PageUtils getAttrWithNoRelation(Map<String, Object> params, Long attrGroupId);

    List<AttrGroupWithAttrsVo> getAttrGroupsWithAttrs(Long catelogId);

    List<AttrGroupEntity> getAttrGroupByCatalogId(Long catalogId);
}

