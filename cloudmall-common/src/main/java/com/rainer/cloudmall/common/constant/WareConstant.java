package com.rainer.cloudmall.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class WareConstant {
    private WareConstant() {

    }

    @Getter
    @AllArgsConstructor
    public enum PurchaseStatus {
        CREATED(0, "新建"),
        ASSIGNED(1, "分配"),
        RECEIVED(2, "已领取"),
        FINISHED(3, "已完成"),
        ERROR(4, "有异常");
        private final int code;

        private final String msg;
    }

    @Getter
    @AllArgsConstructor
    public enum PurchaseDeatilStatus {
        CREATED(0, "新建"),
        ASSIGNED(1, "已分配"),
        BUYING(2, "正在采购"),
        FINISHED(3, "已完成"),
        FAILURE(4, "采购失败");
        private final int code;

        private final String msg;
    }
}
