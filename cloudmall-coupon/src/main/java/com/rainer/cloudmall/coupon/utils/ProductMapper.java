package com.rainer.cloudmall.coupon.utils;

import com.rainer.cloudmall.common.to.SkuReductionTo;
import com.rainer.cloudmall.common.to.SpuBoundsTo;
import com.rainer.cloudmall.coupon.entity.MemberPriceEntity;
import com.rainer.cloudmall.coupon.entity.SkuFullReductionEntity;
import com.rainer.cloudmall.coupon.entity.SkuLadderEntity;
import com.rainer.cloudmall.coupon.entity.SpuBoundsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "work", ignore = true)
    @Mapping(target = "id", ignore = true)
    SpuBoundsEntity spuBoundsToToSpuBoundsEntity(SpuBoundsTo spuBoundsTo);

    @Mapping(target = "addOther", source = "priceStatus")
    @Mapping(target = "id", ignore = true)
    SkuFullReductionEntity skuReductionToToSkuFullReductionEntity(SkuReductionTo skuReductionTo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "addOther", source = "countStatus")
    SkuLadderEntity skuReductionToTOSkuLadderEntity(SkuReductionTo skuReductionTo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addOther", constant = "1")
    MemberPriceEntity memberPriceToToMemberPriceEntity(SkuReductionTo.MemberPriceTo memberPriceTo, Long skuId);

    default List<MemberPriceEntity> memberPriceTosToMemberPriceEntities(List<SkuReductionTo.MemberPriceTo> memberPriceTos, Long skuId) {
        if (CollectionUtils.isEmpty(memberPriceTos)) {
            return Collections.emptyList();
        }
        return memberPriceTos.stream()
                .map(to -> memberPriceToToMemberPriceEntity(to, skuId))
                .toList();
    }
}
