package com.io.hhplus.concert.domain.customer.model;

import com.io.hhplus.concert.common.enums.PointType;

import java.math.BigDecimal;
import java.util.Date;

public record CustomerPointHistory(
        Long customerId,
        BigDecimal pointAmount,
        PointType pointType,
        Date createdAt
) {
    public static CustomerPointHistory create(Long customerId, BigDecimal pointAmount, PointType pointType, Date createdAt) {
        return new CustomerPointHistory(customerId, pointAmount, pointType, createdAt);
    }

    public static boolean isInSufficientPointAmount(BigDecimal pointAmount) {
        return pointAmount == null || pointAmount.compareTo(BigDecimal.ZERO) <= 0;
    }

    public static boolean isAvailablePointTypeWhenCharge(PointType pointType) {
        return pointType != null && pointType.equals(PointType.CHARGE);
    }
}
