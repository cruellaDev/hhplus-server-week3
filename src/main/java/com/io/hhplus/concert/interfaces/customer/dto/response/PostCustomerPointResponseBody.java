package com.io.hhplus.concert.interfaces.customer.dto.response;

import java.math.BigDecimal;

public record PostCustomerPointResponseBody(
        Long customerId,
        BigDecimal balance
) {
}
