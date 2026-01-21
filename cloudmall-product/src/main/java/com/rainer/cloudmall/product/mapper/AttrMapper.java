package com.rainer.cloudmall.product.mapper;

import com.rainer.cloudmall.product.entity.AttrAttrgroupRelationEntity;
import com.rainer.cloudmall.product.entity.AttrEntity;
import com.rainer.cloudmall.product.vo.AttrGroupRelationVo;
import com.rainer.cloudmall.product.vo.AttrResVo;
import com.rainer.cloudmall.product.vo.AttrVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttrMapper {

    AttrEntity attrVoToAttrEntity(AttrVo attrVo);

    AttrResVo attrEntityToAttrResVo(AttrEntity attrEntity);

    AttrAttrgroupRelationEntity attrGroupRelationVoToAttrAttrgroupRelationEntity(AttrGroupRelationVo attrGroupRelationVo);
}
