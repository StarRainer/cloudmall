package com.rainer.cloudmall.product.mapper;

import com.rainer.cloudmall.product.entity.AttrEntity;
import com.rainer.cloudmall.product.vo.AttrVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttrMapper {
    AttrVo attrEntityToAttrVo(AttrEntity attrEntity);

    AttrEntity attrVoToAttrEntity(AttrVo attrVo);
}
