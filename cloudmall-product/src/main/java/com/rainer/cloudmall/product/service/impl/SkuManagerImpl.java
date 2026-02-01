package com.rainer.cloudmall.product.service.impl;

import com.rainer.cloudmall.product.entity.*;
import com.rainer.cloudmall.product.service.*;
import com.rainer.cloudmall.product.vo.SkuItemVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SkuManagerImpl implements SkuManager {
    private final SkuInfoService skuInfoService;
    private final SkuImagesService skuImagesService;
    private final SpuInfoDescService spuInfoDescService;
    private final SkuSaleAttrValueService skuSaleAttrValueService;
    private final AttrGroupService attrGroupService;
    private final AttrAttrgroupRelationService attrAttrgroupRelationService;
    private final ThreadPoolExecutor threadPoolExecutor;

    public SkuManagerImpl(SkuInfoService skuInfoService,
                          SkuImagesService skuImagesService,
                          SpuInfoDescService spuInfoDescService,
                          SkuSaleAttrValueService skuSaleAttrValueService,
                          AttrGroupService attrGroupService,
                          AttrAttrgroupRelationService attrAttrgroupRelationService, ThreadPoolExecutor threadPoolExecutor) {
        this.skuInfoService = skuInfoService;
        this.skuImagesService = skuImagesService;
        this.spuInfoDescService = spuInfoDescService;
        this.skuSaleAttrValueService = skuSaleAttrValueService;
        this.attrGroupService = attrGroupService;
        this.attrAttrgroupRelationService = attrAttrgroupRelationService;
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Override
    public SkuItemVo getItemDetail(Long skuId) {
        SkuItemVo skuItemVo = new SkuItemVo();

        CompletableFuture<SkuInfoEntity> skuInfoEntityFuture = CompletableFuture.supplyAsync(() -> {
            // 获取sku基本信息
            SkuInfoEntity skuInfoEntity = skuInfoService.getById(skuId);
            skuItemVo.setSkuInfoEntity(skuInfoEntity);
            return skuInfoEntity;
        }, threadPoolExecutor);

        CompletableFuture<Void> saleAttrFuture = skuInfoEntityFuture.thenAcceptAsync(skuInfoEntity -> {
            // 获取spu销售属性组合
            List<Long> skuIds = skuInfoService.getSkuIdsByspuId(skuInfoEntity.getSpuId());
            List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = skuSaleAttrValueService.getAttrValuesBySkuIds(skuIds);
            List<SkuItemVo.SkuItemSaleAttrVo> skuItemSaleAttrVos = skuSaleAttrValueEntities
                    .stream()
                    .collect(Collectors.groupingBy(SkuSaleAttrValueEntity::getAttrId))
                    .entrySet().stream()
                    .map(entry -> {
                        SkuItemVo.SkuItemSaleAttrVo skuItemSaleAttrVo = new SkuItemVo.SkuItemSaleAttrVo();
                        skuItemSaleAttrVo.setAttrId(entry.getKey());
                        List<SkuSaleAttrValueEntity> groupEntities = entry.getValue();
                        skuItemSaleAttrVo.setAttrName(groupEntities.getFirst().getAttrName());
                        List<SkuItemVo.SkuItemSaleAttrVo.AttrValueWithSkuIdVo> attrValueVos = groupEntities.stream()
                                .collect(Collectors.groupingBy(SkuSaleAttrValueEntity::getAttrValue))
                                .entrySet().stream()
                                .map(valEntry -> {
                                    SkuItemVo.SkuItemSaleAttrVo.AttrValueWithSkuIdVo valVo =
                                            new SkuItemVo.SkuItemSaleAttrVo.AttrValueWithSkuIdVo();
                                    valVo.setAttrValue(valEntry.getKey());
                                    String skuIdsStr = valEntry.getValue().stream()
                                            .map(entity -> entity.getSkuId().toString())
                                            .collect(Collectors.joining(","));
                                    valVo.setSkuIds(skuIdsStr);
                                    return valVo;
                                })
                                .toList();
                        skuItemSaleAttrVo.setAttrValues(attrValueVos);
                        return skuItemSaleAttrVo;
                    })
                    .toList();
            skuItemVo.setSaleAttrVos(skuItemSaleAttrVos);
        }, threadPoolExecutor);

        CompletableFuture<Void> descriptionFuture = skuInfoEntityFuture.thenAcceptAsync(skuInfoEntity -> {
            // 获取spu介绍
            SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(skuInfoEntity.getSpuId());
            skuItemVo.setSpuInfoDescEntity(spuInfoDescEntity);
        }, threadPoolExecutor);

        CompletableFuture<Void> baseAttrFuture = skuInfoEntityFuture.thenAcceptAsync(skuInfoEntity -> {
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
        }, threadPoolExecutor);

        CompletableFuture<Void> skuImagesEntityFuture = CompletableFuture.runAsync(() -> {
            // 获取sku图片信息
            List<SkuImagesEntity> skuImagesEntities = skuImagesService.getImagesBySkuId(skuId);
            skuItemVo.setImagesEntities(skuImagesEntities);
        }, threadPoolExecutor);

        CompletableFuture.allOf(saleAttrFuture, baseAttrFuture, descriptionFuture, skuImagesEntityFuture).join();

        return skuItemVo;
    }
}
