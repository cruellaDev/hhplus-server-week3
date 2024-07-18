package com.io.hhplus.concert.domain.customer.service.model;

import com.io.hhplus.concert.common.enums.PointType;

import java.math.BigDecimal;
import java.util.Date;

public record CustomerPointHistoryModel(
        Long customerId,
        BigDecimal pointAmount,
        PointType pointType,
        Date createdAt
) {
    public static CustomerPointHistoryModel create(Long customerId, BigDecimal pointAmount, PointType pointType, Date createdAt) {
        return new CustomerPointHistoryModel(customerId, pointAmount, pointType, createdAt);
    }

    public static CustomerPointHistoryModel noContents() {
        return create(-1L, BigDecimal.ZERO, null, null);
    }
}
