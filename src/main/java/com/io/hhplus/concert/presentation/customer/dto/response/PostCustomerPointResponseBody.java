package com.io.hhplus.concert.presentation.customer.dto.response;

import java.math.BigDecimal;

public record PostCustomerPointResponseBody(
        Long customerId,
        BigDecimal balance
) {
}
