package com.io.hhplus.concert.presentation.reservation.dto;

import java.math.BigDecimal;

public record PayDto(
        String payMethod,
        BigDecimal payAmount
) {
}
