package com.io.hhplus.concert.domain.concert.model;

import java.math.BigDecimal;
import java.util.Date;

public record Performance(
        Long performanceId,
        BigDecimal price,
        Integer capacityLimit,
        Date performedAt
) {
    public static Performance create(Long performanceId, BigDecimal price, Integer capacityLimit, Date performedAt) {
        return new Performance(performanceId, price, capacityLimit, performedAt);
    }

    public static Performance noContents() {
        return create(-1L, BigDecimal.ZERO, 0, null);
    }

    // select, update 시에만 사용
    public static boolean isAvailablePerformanceId(Long performanceId) {
        return performanceId != null && performanceId.compareTo(0L) > 0;
    }

    public static boolean isAvailableCapacityLimit(Integer capacityLimit) {
        return capacityLimit != null && capacityLimit > 0;
    }

    public static boolean isPerformAtAfterTargetDate(Date performedAt, Date targetDate) {
        return performedAt != null && targetDate != null && performedAt.after(targetDate);
    }
}
