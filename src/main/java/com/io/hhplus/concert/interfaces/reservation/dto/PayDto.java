package com.io.hhplus.concert.interfaces.reservation.dto;

import java.math.BigDecimal;

public record PayDto(
        String payMethod,
        BigDecimal payAmount
) {
}
