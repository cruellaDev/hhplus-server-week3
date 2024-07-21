package com.io.hhplus.concert.domain.concert.service.model;

import java.math.BigDecimal;
import java.util.Date;

public record PerformanceModel(
        Long performanceId,
        BigDecimal price,
        Integer capacityLimit,
        Date performedAt
) {
    public static PerformanceModel create(Long performanceId, BigDecimal price, Integer capacityLimit, Date performedAt) {
        return new PerformanceModel(performanceId, price, capacityLimit, performedAt);
    }

    public static PerformanceModel noContents() {
        return create(-1L, BigDecimal.ZERO, 0, null);
    }

}
