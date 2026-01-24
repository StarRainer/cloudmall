package com.rainer.cloudmall.ware.utils;


import com.rainer.cloudmall.ware.entity.PurchaseDetailEntity;
import com.rainer.cloudmall.ware.entity.WareSkuEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WareMapper {
    @Mapping(target = "stockLocked", ignore = true)
    @Mapping(target = "skuName", ignore = true)
    @Mapping(target = "stock", source = "skuNum")
    @Mapping(target = "id", ignore = true)
    WareSkuEntity toWareSkuEntity(PurchaseDetailEntity purchaseDetailEntity);

    List<WareSkuEntity> toWareSkuEntities(List<PurchaseDetailEntity> purchaseDetailEntities);
}
