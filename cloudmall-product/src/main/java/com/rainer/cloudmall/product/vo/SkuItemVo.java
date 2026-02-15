package com.rainer.cloudmall.product.vo;

import com.rainer.cloudmall.product.entity.SkuImagesEntity;
import com.rainer.cloudmall.product.entity.SkuInfoEntity;
import com.rainer.cloudmall.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuItemVo {
    private SkuInfoEntity skuInfoEntity;
    private List<SkuImagesEntity> imagesEntities;
    private List<SkuItemSaleAttrVo> saleAttrVos;
    private SpuInfoDescEntity spuInfoDescEntity;
    private List<SpuItemAttrGroupVo> spuItemAttrGroupVos;
    private boolean hasStock = true;
    private SeckillSkuVo seckillSkuVo;

    @Data
    public static class SkuItemSaleAttrVo {
        private Long attrId;
        private String attrName;
        private List<AttrValueWithSkuIdVo> attrValues;

        @Data
        public static class AttrValueWithSkuIdVo {
            private String attrValue;
            private String skuIds;
        }
    }

    @Data
    public static class SpuItemAttrGroupVo {
        private String groupName;
        private List<SpuBaseAttrVo> spuBaseAttrVos;

        @Data
        public static class SpuBaseAttrVo {
            private String attrName;
            private String attrValue;
        }
    }

    @Data
    public static class SeckillSkuVo {
        private Long promotionId;
        private Long promotionSessionId;
        private Long skuId;
        private BigDecimal seckillPrice;
        private Integer seckillCount;
        private Integer seckillLimit;
        private Integer seckillSort;
        private Long startTime;
        private Long endTime;
        private String randomCode;
    }
}
