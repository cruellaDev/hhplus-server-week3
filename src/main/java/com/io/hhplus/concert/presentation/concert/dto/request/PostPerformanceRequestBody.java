package com.io.hhplus.concert.presentation.concert.dto.request;

import java.math.BigDecimal;

public record PostPerformanceRequestBody(
        Long performanceId,
        Long seatId,
        String seatNo,
        BigDecimal price
) {
}
