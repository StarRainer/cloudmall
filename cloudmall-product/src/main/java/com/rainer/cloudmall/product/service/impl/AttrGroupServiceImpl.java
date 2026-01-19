package com.rainer.cloudmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Query;

import com.rainer.cloudmall.product.dao.AttrGroupDao;
import com.rainer.cloudmall.product.entity.AttrGroupEntity;
import com.rainer.cloudmall.product.service.AttrGroupService;
import org.springframework.util.StringUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

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
        return new PageUtils(page(
                new Query<AttrGroupEntity>().getPage(params),
                new LambdaQueryWrapper<AttrGroupEntity>()
                        .eq(catelogId != 0, AttrGroupEntity::getCatelogId, catelogId)
                        .and(StringUtils.hasText(key), object ->
                                object.eq(key.matches("^\\d*$"), AttrGroupEntity::getAttrGroupId, key).or().like(AttrGroupEntity::getAttrGroupName, key)
                        )
        ));
    }

}