package com.rainer.cloudmall.product.utils;

import com.rainer.cloudmall.product.entity.*;
import com.rainer.cloudmall.product.vo.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    AttrEntity attrVoToAttrEntity(AttrVo attrVo);

    @Mapping(target = "groupName", ignore = true)
    @Mapping(target = "catelogPath", ignore = true)
    @Mapping(target = "catelogName", ignore = true)
    @Mapping(target = "attrGroupId", ignore = true)
    AttrResVo attrEntityToAttrResVo(AttrEntity attrEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "attrSort", ignore = true)
    AttrAttrgroupRelationEntity attrGroupRealitonVoToAttrAttrgroupRelationEntity(AttrGroupRelationVo attrGroupRelationVo);

    @Mapping(target = "brandName", source = "name")
    BrandResVo brandEntityToBrandResVo(BrandEntity brandEntity);

    @Mapping(target = "attrs", ignore = true)
    AttrGroupWithAttrsVo attrGroupEntityToAttrGroupWithAttrsVo(AttrGroupEntity attrGroupEntity);

    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    SpuInfoEntity spuSaveVoToSpuInfoEntity(SpuSaveVo spuSaveVo);

    @Named("joinStringList")
    default String joinStringList(List<String> decript) {
        if (CollectionUtils.isEmpty(decript)) {
            return null;
        }
        return String.join(",", decript);
    }

    @Mapping(target = "decript", source = "spuSaveVo.decript", qualifiedByName = "joinStringList")
    SpuInfoDescEntity spuSaveVoToSpuInfoDescEntity(SpuSaveVo spuSaveVo, Long spuId);

    default List<SpuImagesEntity> imagesToSpuImagesEntities(List<String> images, Long spuId) {
        if (CollectionUtils.isEmpty(images)) {
            return Collections.emptyList();
        }
        return images.stream()
                .map(image -> stringToSpuImagesEntity(image, spuId))
                .toList();
    }

    @Mapping(target = "imgUrl", source = "imgUrl")
    @Mapping(target = "imgSort", ignore = true)
    @Mapping(target = "imgName", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "defaultImg", ignore = true)
    SpuImagesEntity stringToSpuImagesEntity(String imgUrl, Long spuId);

    @Mapping(target = "attrName", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "attrSort", ignore = true)
    @Mapping(target = "attrValue", source = "baseAttrsVo.attrValues")
    @Mapping(target = "quickShow", source = "baseAttrsVo.showDesc")
    ProductAttrValueEntity baseAttrVoToProductAttrValueEntity(SpuSaveVo.BaseAttrsVo baseAttrsVo, Long spuId);

    default List<ProductAttrValueEntity> baseAttrsVosToProductAttrValueEntities(List<SpuSaveVo.BaseAttrsVo> baseAttrsVos, Long spuId) {
        if (CollectionUtils.isEmpty(baseAttrsVos)) {
            return Collections.emptyList();
        }
        return baseAttrsVos.stream()
                .map(entity -> baseAttrVoToProductAttrValueEntity(entity, spuId))
                .toList();
    }

    @Mapping(target = "skuDesc", source = "skusVo.descar", qualifiedByName = "joinStringList")
    @Mapping(target = "skuId", ignore = true)
    @Mapping(source = "skusVo.images", target = "skuDefaultImg", qualifiedByName = "imagesVosToString")
    @Mapping(source = "spuInfoEntity.id", target = "spuId")
    @Mapping(constant = "0L", target = "saleCount")
    SkuInfoEntity skusVoAndSpuInfoEntityToSkuInfoEntity(SpuSaveVo.SkusVo skusVo, SpuInfoEntity spuInfoEntity);

    default List<SkuInfoEntity> skusVosAndSpuInfoEntityToSkuInfoEntities(List<SpuSaveVo.SkusVo> skusVos, SpuInfoEntity spuInfoEntity) {
        if (CollectionUtils.isEmpty(skusVos)) {
            return Collections.emptyList();
        }
        return skusVos.stream()
                .map(entity -> skusVoAndSpuInfoEntityToSkuInfoEntity(entity, spuInfoEntity))
                .toList();
    }

    @Named("imagesVosToString")
    default String imagesVosToString(List<SpuSaveVo.SkusVo.ImagesVo> imagesVos) {
        if (CollectionUtils.isEmpty(imagesVos)) {
            return null;
        }

        SpuSaveVo.SkusVo.ImagesVo defaultImagesVo = null;
        for (SpuSaveVo.SkusVo.ImagesVo imagesVo : imagesVos) {
            if (imagesVo.getDefaultImg() != null && imagesVo.getDefaultImg() == 1) {
                defaultImagesVo = imagesVo;
                break;
            }
        }

        if (defaultImagesVo != null) {
            return defaultImagesVo.getImgUrl();
        }

        for (SpuSaveVo.SkusVo.ImagesVo imagesVo : imagesVos) {
            if (StringUtils.hasText(imagesVo.getImgUrl())) {
                defaultImagesVo = imagesVo;
                break;
            }
        }
        return defaultImagesVo == null ? null : defaultImagesVo.getImgUrl();
    }

    @Mapping(target = "imgSort", ignore = true)
    @Mapping(target = "id", ignore = true)
    SkuImagesEntity imagesVoToSkuImagesEntity(SpuSaveVo.SkusVo.ImagesVo imagesVo, Long skuId);

    default List<SkuImagesEntity> imagesVosToSkuImagesEntities(List<SpuSaveVo.SkusVo.ImagesVo> imagesVos, Long skuId) {
        if (CollectionUtils.isEmpty(imagesVos)) {
            return Collections.emptyList();
        }
        return imagesVos.stream()
                .map(entity -> imagesVoToSkuImagesEntity(entity, skuId))
                .toList();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "attrSort", ignore = true)
    SkuSaleAttrValueEntity attrVoToSkuSaleAttrValueEntity(SpuSaveVo.SkusVo.AttrVo attrVo, Long skuId);

    default List<SkuSaleAttrValueEntity> attrVosToSkuSaleAttrValueEntities(List<SpuSaveVo.SkusVo.AttrVo> attrVo, Long skuId) {
        if (CollectionUtils.isEmpty(attrVo)) {
            return Collections.emptyList();
        }
        return attrVo.stream()
                .map(entity -> attrVoToSkuSaleAttrValueEntity(entity, skuId))
                .toList();
    }

    @Mapping(target = "id", source = "categoryEntity.catId")
    @Mapping(target = "catalog3List", source = "catalog3Vos")
    @Mapping(target = "catalog1Id", source = "categoryEntity.parentCid")
    Catelog2Vo toCateLog2Vo(CategoryEntity categoryEntity, List<Catelog2Vo.Catelog3Vo> catalog3Vos);

    @Mapping(target = "catalog2Id", source = "categoryEntity.parentCid")
    @Mapping(target = "id", source = "categoryEntity.catId")
    Catelog2Vo.Catelog3Vo toCatelog3Vo(CategoryEntity categoryEntity);
}
