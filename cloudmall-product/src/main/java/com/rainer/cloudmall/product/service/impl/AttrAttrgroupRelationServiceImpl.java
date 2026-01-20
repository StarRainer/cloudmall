package com.rainer.cloudmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Query;
import com.rainer.cloudmall.product.dao.AttrAttrgroupRelationDao;
import com.rainer.cloudmall.product.dao.AttrDao;
import com.rainer.cloudmall.product.dao.AttrGroupDao;
import com.rainer.cloudmall.product.entity.AttrAttrgroupRelationEntity;
import com.rainer.cloudmall.product.entity.AttrEntity;
import com.rainer.cloudmall.product.entity.AttrGroupEntity;
import com.rainer.cloudmall.product.service.AttrAttrgroupRelationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    private final AttrDao attrDao;

    private final AttrGroupDao attrGroupDao;

    public AttrAttrgroupRelationServiceImpl(AttrDao attrDao, AttrGroupDao attrGroupDao) {
        this.attrDao = attrDao;
        this.attrGroupDao = attrGroupDao;
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
        return attrDao.selectByIds(attrIds);
    }

    @Override
    public AttrGroupEntity getAttrGroupByAttrGroupId(Long attrGroupId) {
        return attrGroupDao.selectById(attrGroupId);
    }

}