package com.rainer.cloudmall.product.service.impl;

import com.rainer.cloudmall.product.entity.AttrAttrgroupRelationEntity;
import com.rainer.cloudmall.product.mapper.AttrMapper;
import com.rainer.cloudmall.product.service.AttrAttrgroupRelationService;
import com.rainer.cloudmall.product.vo.AttrVo;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Query;

import com.rainer.cloudmall.product.dao.AttrDao;
import com.rainer.cloudmall.product.entity.AttrEntity;
import com.rainer.cloudmall.product.service.AttrService;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    private final AttrAttrgroupRelationService attrAttrgroupRelationService;

    private final AttrMapper attrMapper;

    public AttrServiceImpl(AttrAttrgroupRelationService attrAttrgroupRelationService, AttrMapper attrMapper) {
        this.attrAttrgroupRelationService = attrAttrgroupRelationService;
        this.attrMapper = attrMapper;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long attrId) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveAttr(AttrVo attrVo) {
        AttrEntity attrEntity = attrMapper.attrVoToAttrEntity(attrVo);
        save(attrEntity);
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
        attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
        attrAttrgroupRelationEntity.setAttrGroupId(attrVo.getAttrGroupId());
        attrAttrgroupRelationService.save(attrAttrgroupRelationEntity);
    }

}