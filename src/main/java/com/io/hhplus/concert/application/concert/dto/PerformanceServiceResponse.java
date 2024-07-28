package com.io.hhplus.concert.application.concert.dto;

import com.io.hhplus.concert.domain.concert.model.Performance;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Date;

@Builder
public record PerformanceServiceResponse(
        Long performanceId,
        Long concertId,
        BigDecimal performancePrice,
        Integer capacityLimit,
        Date performedAt
) {
    public static PerformanceServiceResponse from(Performance performance) {
        return PerformanceServiceResponse.builder()
                .performanceId(performance.performanceId())
                .concertId(performance.concertId())
                .performancePrice(performance.performancePrice())
                .capacityLimit(performance.capacityLimit())
                .performedAt(performance.performedAt())
                .build();
    }
}
