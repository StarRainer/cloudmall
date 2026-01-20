package com.rainer.cloudmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainer.cloudmall.common.constant.ProductConstant;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Query;
import com.rainer.cloudmall.product.dao.AttrDao;
import com.rainer.cloudmall.product.entity.AttrAttrgroupRelationEntity;
import com.rainer.cloudmall.product.entity.AttrEntity;
import com.rainer.cloudmall.product.entity.AttrGroupEntity;
import com.rainer.cloudmall.product.entity.CategoryEntity;
import com.rainer.cloudmall.product.mapper.AttrMapper;
import com.rainer.cloudmall.product.service.AttrAttrgroupRelationService;
import com.rainer.cloudmall.product.service.AttrGroupService;
import com.rainer.cloudmall.product.service.AttrService;
import com.rainer.cloudmall.product.service.CategoryService;
import com.rainer.cloudmall.product.vo.AttrResVo;
import com.rainer.cloudmall.product.vo.AttrVo;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    private final AttrGroupService attrGroupService;

    private final CategoryService categoryService;

    private final AttrAttrgroupRelationService attrAttrgroupRelationService;

    private final AttrMapper attrMapper;

    public AttrServiceImpl(AttrGroupService attrGroupService,
                           CategoryService categoryService,
                           AttrAttrgroupRelationService attrAttrgroupRelationService,
                           AttrMapper attrMapper) {
        this.attrGroupService = attrGroupService;
        this.categoryService = categoryService;
        this.attrAttrgroupRelationService = attrAttrgroupRelationService;
        this.attrMapper = attrMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public PageUtils queryPage(Map<String, Object> params, Long cateLogId, String attrType) {
        String key = (String) params.get("key");
        boolean isNumber = key != null && key.matches("^\\d+$");
        IPage<AttrEntity> page = page(
                new Query<AttrEntity>().getPage(params),
                new LambdaQueryWrapper<AttrEntity>()
                        .eq(cateLogId != 0, AttrEntity::getCatelogId, cateLogId)
                        .eq(AttrEntity::getAttrType,
                                ProductConstant.AttrType.BASE.getMsg().equalsIgnoreCase(attrType)
                                        ? ProductConstant.AttrType.BASE.getCode()
                                        : ProductConstant.AttrType.SALE.getCode())
                        .and(StringUtils.hasLength(key), object ->
                                object.eq(isNumber, AttrEntity::getAttrId, isNumber ? Long.parseLong(key) : null)
                                        .or().like(AttrEntity::getAttrName, key)
                        )
        );
        PageUtils pageUtils = new PageUtils(page);
        List<AttrResVo> records = page.getRecords()
                .stream()
                .map(attrEntity -> {
                    // 映射到 VO
                    AttrResVo attrResVo = attrMapper.attrEntityToAttrShowVo(attrEntity);

                    // 获取所属分类
                    attrResVo.setCatelogName(categoryService.getById(attrEntity.getCatelogId()).getName());

                    // 获取所属分组
                    AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationService
                            .getOne(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                                    .eq(AttrAttrgroupRelationEntity::getAttrId, attrEntity.getAttrId())
                            );
                    if (attrAttrgroupRelationEntity != null) {
                        Long attrGroupId = attrAttrgroupRelationEntity.getAttrGroupId();
                        AttrGroupEntity attrGroupEntity = attrGroupService.getById(attrGroupId);
                        if (attrGroupEntity != null) {
                            attrResVo.setGroupName(attrGroupEntity.getAttrGroupName());
                        }
                    }
                    return attrResVo;
                })
                .toList();
        pageUtils.setList(records);
        return pageUtils;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAttr(AttrVo attrVo) {
        AttrEntity attrEntity = attrMapper.attrVoToAttrEntity(attrVo);
        save(attrEntity);

        Long groupId = attrVo.getCatelogId();
        if (attrVo.getAttrGroupId() != null) {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationEntity.setAttrGroupId(groupId);
            attrAttrgroupRelationService.save(attrAttrgroupRelationEntity);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public AttrResVo getAttrInfo(Long attrId) {
        AttrEntity attrEntity = getById(attrId);
        AttrResVo attrResVo = attrMapper.attrEntityToAttrShowVo(attrEntity);

        // 获取属性组名和组ID
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationService.getOne(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                .eq(AttrAttrgroupRelationEntity::getAttrId, attrId)
        );
        if (attrAttrgroupRelationEntity != null) {
            Long groupId = attrAttrgroupRelationEntity.getAttrGroupId();
            attrResVo.setAttrGroupId(groupId);
            AttrGroupEntity attrGroupEntity = attrGroupService.getById(groupId);
            if (attrGroupEntity != null) {
                attrResVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
        }

        // 获取分类完整路径
        List<Long> pathLink = categoryService.getPathLink(attrEntity.getCatelogId());
        attrResVo.setCatelogPath(pathLink);

        // 设置分类名
        CategoryEntity categoryEntity = categoryService.getById(attrEntity.getCatelogId());
        if (categoryEntity != null) {
            attrResVo.setCatelogName(categoryEntity.getName());
        }

        return attrResVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateById(AttrVo attrVo) {
        AttrEntity attrEntity = attrMapper.attrVoToAttrEntity(attrVo);
        updateById(attrEntity);

        if (attrEntity.getAttrType() == ProductConstant.AttrType.SALE.getCode()) {
            return;
        }

        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
        attrAttrgroupRelationEntity.setAttrId(attrVo.getAttrId());
        attrAttrgroupRelationEntity.setAttrGroupId(attrVo.getAttrGroupId());

        // 判断是修改还是新增
        long count = attrAttrgroupRelationService.count(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                .eq(AttrAttrgroupRelationEntity::getAttrId, attrAttrgroupRelationEntity.getAttrId()));

        if (count > 0) {
            attrAttrgroupRelationService.update(attrAttrgroupRelationEntity, new LambdaUpdateWrapper<AttrAttrgroupRelationEntity>()
                    .eq(AttrAttrgroupRelationEntity::getAttrId, attrAttrgroupRelationEntity.getAttrId()));
        } else {
            attrAttrgroupRelationService.save(attrAttrgroupRelationEntity);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAttr(List<Long> attrIds) {
        attrAttrgroupRelationService.remove(
                new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                        .in(AttrAttrgroupRelationEntity::getAttrId, attrIds)
        );
        removeByIds(attrIds);
    }

}