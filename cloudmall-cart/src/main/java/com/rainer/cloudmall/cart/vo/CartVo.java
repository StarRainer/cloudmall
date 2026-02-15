package com.rainer.cloudmall.cart.vo;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Data
public class CartVo {
    private List<CartItemVo> items;
    private BigDecimal reduce = BigDecimal.ZERO;

    public Integer getCountNum() {
        if (CollectionUtils.isEmpty(items)) {
            return 0;
        }
        return items.stream()
                .filter(Objects::nonNull)
                .mapToInt(item -> item.getCount() == null ? 0 : item.getCount())
                .sum();
    }

    public Integer getCountType() {
        if (CollectionUtils.isEmpty(items)) {
            return 0;
        }
        return items.size();
    }


    public BigDecimal getTotalAmount() {
        if (CollectionUtils.isEmpty(items)) {
            return BigDecimal.ZERO;
        }
        return items.stream()
                .filter(Objects::nonNull)
                .map(item -> item.getTotalPrice() == null ? BigDecimal.ZERO : item.getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .subtract(getReduce() == null ? BigDecimal.ZERO : getReduce());
    }
}
