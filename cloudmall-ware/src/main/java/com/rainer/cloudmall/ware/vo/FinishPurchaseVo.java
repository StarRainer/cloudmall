package com.rainer.cloudmall.ware.vo;

import lombok.Data;

import java.util.List;

@Data
public class FinishPurchaseVo {
    private Long id;

    private List<ItemVo> items;

    @Data
    public static class ItemVo {
        private Long itemId;

        private Integer status;

        private String reason;
    }
}
