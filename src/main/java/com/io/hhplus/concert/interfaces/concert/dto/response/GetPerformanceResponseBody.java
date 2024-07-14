package com.io.hhplus.concert.interfaces.concert.dto.response;

import java.math.BigDecimal;
import java.util.Date;

public record GetPerformanceResponseBody (
        Long performanceId,
        Date performAt,
        BigDecimal price,
        Integer capacityLimit
) {
}
