package com.rainer.cloudmall.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuReductionTo {
    private Long skuId;
    private Integer fullCount;
    private BigDecimal discount;
    private Integer countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private Integer priceStatus;
    private List<MemberPriceTo> memberPrice;

    @Data
    public static class MemberPriceTo {
        private Long memberLevelId;
        private String memberLevelName;
        private BigDecimal memberPrice;
    }
}
