package com.rainer.cloudmall.product.utils;

import com.rainer.cloudmall.product.entity.AttrAttrgroupRelationEntity;
import com.rainer.cloudmall.product.entity.AttrEntity;
import com.rainer.cloudmall.product.entity.AttrGroupEntity;
import com.rainer.cloudmall.product.entity.BrandEntity;
import com.rainer.cloudmall.product.vo.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    AttrEntity attrVoToAttrEntity(AttrVo attrVo);

    AttrResVo attrEntityToAttrResVo(AttrEntity attrEntity);

    AttrAttrgroupRelationEntity attrGroupRelationVoToAttrAttrgroupRelationEntity(AttrGroupRelationVo attrGroupRelationVo);

    @Mapping(target = "brandName", source = "name")
    BrandResVo brandEntityToBrandResVo(BrandEntity brandEntity);

    AttrGroupWithAttrsVo attrGroupEntityToAttrGroupWithAttrsVo(AttrGroupEntity attrGroupEntity);
}
