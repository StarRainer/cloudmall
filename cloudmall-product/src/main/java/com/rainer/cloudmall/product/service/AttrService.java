package com.rainer.cloudmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.product.entity.AttrEntity;
import com.rainer.cloudmall.product.vo.AttrResVo;
import com.rainer.cloudmall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 11:20:39
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params, Long cateLogId);

    void saveAttr(AttrVo attrVo);

    AttrResVo getAttrInfo(Long attrId);

    void updateById(AttrVo attr);

    void deleteAttr(List<Long> attrIds);
}

