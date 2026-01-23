package com.rainer.cloudmall.product.utils;

import com.rainer.cloudmall.common.to.SkuReductionTo;
import com.rainer.cloudmall.common.to.SpuBoundsTo;
import com.rainer.cloudmall.product.vo.SpuSaveVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface CouponMapper {
    SpuBoundsTo boundsVoToSpuBoundsTo(SpuSaveVo.BoundsVo boundsVo, Long spuId);

    @Mapping(source = "id", target = "memberLevelId")
    @Mapping(source = "name", target = "memberLevelName")
    @Mapping(source = "price", target = "memberPrice")
    SkuReductionTo.MemberPriceTo memberPriceVoToMemberPriceVo(SpuSaveVo.SkusVo.MemberPriceVo memberPriceVo);

    SkuReductionTo skusVoToSkuReductionVo(SpuSaveVo.SkusVo skusVo, Long skuId);
}
