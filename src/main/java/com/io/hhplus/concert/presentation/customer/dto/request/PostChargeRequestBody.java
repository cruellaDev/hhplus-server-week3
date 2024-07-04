package com.io.hhplus.concert.presentation.customer.dto.request;

import java.math.BigDecimal;

public record PostChargeRequestBody(
        Long customerId,
        BigDecimal amount
) {
}
