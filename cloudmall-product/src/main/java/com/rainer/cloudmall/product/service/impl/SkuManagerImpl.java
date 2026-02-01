package com.rainer.cloudmall.product.service.impl;

import com.rainer.cloudmall.product.entity.*;
import com.rainer.cloudmall.product.service.*;
import com.rainer.cloudmall.product.vo.SkuItemVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class SkuManagerImpl implements SkuManager {
    private final SkuInfoService skuInfoService;
    private final SkuImagesService skuImagesService;
    private final SpuInfoDescService spuInfoDescService;
    private final SkuSaleAttrValueService skuSaleAttrValueService;
    private final AttrGroupService attrGroupService;
    private final AttrAttrgroupRelationService attrAttrgroupRelationService;

    public SkuManagerImpl(SkuInfoService skuInfoService,
                          SkuImagesService skuImagesService,
                          SpuInfoDescService spuInfoDescService,
                          SkuSaleAttrValueService skuSaleAttrValueService,
                          AttrGroupService attrGroupService,
                          AttrAttrgroupRelationService attrAttrgroupRelationService) {
        this.skuInfoService = skuInfoService;
        this.skuImagesService = skuImagesService;
        this.spuInfoDescService = spuInfoDescService;
        this.skuSaleAttrValueService = skuSaleAttrValueService;
        this.attrGroupService = attrGroupService;
        this.attrAttrgroupRelationService = attrAttrgroupRelationService;
    }

    @Override
    public SkuItemVo getItemDetail(Long skuId) {
        SkuItemVo skuItemVo = new SkuItemVo();

        // 获取sku基本信息
        SkuInfoEntity skuInfoEntity = skuInfoService.getById(skuId);
        skuItemVo.setSkuInfoEntity(skuInfoEntity);

        // 获取sku图片信息
        List<SkuImagesEntity> skuImagesEntities = skuImagesService.getImagesBySkuId(skuId);
        skuItemVo.setImagesEntities(skuImagesEntities);

        // 获取spu销售属性组合
        List<Long> skuIds = skuInfoService.getSkuIdsByspuId(skuInfoEntity.getSpuId());
        List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = skuSaleAttrValueService.getAttrValuesBySkuIds(skuIds);
        List<SkuItemVo.SkuItemSaleAttrVo> skuItemSaleAttrVos = skuSaleAttrValueEntities.stream()
                .collect(Collectors.groupingBy(SkuSaleAttrValueEntity::getAttrId))
                .entrySet().stream()
                .map(entry -> {
                    SkuItemVo.SkuItemSaleAttrVo skuItemSaleAttrVo = new SkuItemVo.SkuItemSaleAttrVo();
                    skuItemSaleAttrVo.setAttrId(entry.getKey());
                    skuItemSaleAttrVo.setAttrName(entry.getValue().getFirst().getAttrName());
                    skuItemSaleAttrVo.setAttrValues(entry.getValue()
                            .stream()
                            .map(SkuSaleAttrValueEntity::getAttrValue)
                            .distinct()
                            .toList()
                    );
                    return skuItemSaleAttrVo;
                })
                .toList();
        skuItemVo.setSaleAttrVos(skuItemSaleAttrVos);

        // 获取spu介绍
        SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(skuInfoEntity.getSpuId());
        skuItemVo.setSpuInfoDescEntity(spuInfoDescEntity);

        // 获取spu规格参数信息
        List<AttrGroupEntity> attrGroupEntities = attrGroupService.getAttrGroupByCatalogId(skuInfoEntity.getCatalogId());
        List<Long> groupIds = attrGroupEntities
                .stream()
                .map(AttrGroupEntity::getAttrGroupId)
                .toList();
        List<AttrAttrgroupRelationEntity> attrAttrGroupRelationEntities = attrAttrgroupRelationService.getAttrAttrGroupRelationsByAttrGroupIds(groupIds);
        List<Long> attrIds = attrAttrGroupRelationEntities
                .stream()
                .map(AttrAttrgroupRelationEntity::getAttrId)
                .toList();
        Map<Long, List<Long>> groupIdToAttrIds = attrAttrGroupRelationEntities
                .stream()
                .collect(Collectors.groupingBy(
                        AttrAttrgroupRelationEntity::getAttrGroupId,
                        Collectors.mapping(
                                AttrAttrgroupRelationEntity::getAttrId,
                                Collectors.toList()
                        )
                ));
        Map<Long, AttrEntity> attrIdToAttr = attrAttrgroupRelationService.getAttrsByAttrIds(attrIds)
                .stream()
                .collect(Collectors.toMap(AttrEntity::getAttrId, Function.identity()));
        List<SkuItemVo.SpuItemAttrGroupVo> spuItemAttrGroupVos = attrGroupEntities
                .stream()
                .map(attrGroupEntity -> {
                    SkuItemVo.SpuItemAttrGroupVo spuItemAttrGroupVo = new SkuItemVo.SpuItemAttrGroupVo();
                    spuItemAttrGroupVo.setGroupName(attrGroupEntity.getAttrGroupName());
                    spuItemAttrGroupVo.setSpuBaseAttrVos(groupIdToAttrIds
                            .get(attrGroupEntity.getAttrGroupId())
                            .stream()
                            .map(attrId -> {
                                SkuItemVo.SpuItemAttrGroupVo.SpuBaseAttrVo spuBaseAttrVo = new SkuItemVo.SpuItemAttrGroupVo.SpuBaseAttrVo();
                                AttrEntity attrEntity = attrIdToAttr.get(attrId);
                                spuBaseAttrVo.setAttrName(attrEntity.getAttrName());
                                spuBaseAttrVo.setAttrValue(attrEntity.getValueSelect());
                                return spuBaseAttrVo;
                            })
                            .toList()
                    );
                    return spuItemAttrGroupVo;
                })
                .toList();
        skuItemVo.setSpuItemAttrGroupVos(spuItemAttrGroupVos);
        return skuItemVo;
    }
}
