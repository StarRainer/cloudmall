package com.rainer.cloudmall.product.vo;

import lombok.Data;

import java.util.List;

@Data
public class Catelog2Vo {
    private String catalog1Id;

    private List<Object> catalog3List;

    private String id;

    private String name;

    @Data
    public static class Catelog3Vo {
        private String catalog2Id;

        private String id;

        private String name;
    }
}
