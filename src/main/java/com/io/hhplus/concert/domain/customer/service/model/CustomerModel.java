package com.io.hhplus.concert.domain.customer.service.model;

import java.math.BigDecimal;

public record CustomerModel(
        Long customerId,
        String customerName,
        BigDecimal pointBalance
) {
    public static CustomerModel create(Long customerId, String customerName, BigDecimal pointBalance) {
        return new CustomerModel(customerId, customerName, pointBalance);
    }

    public static CustomerModel noContents() {
        return create(-1L, "N/A", BigDecimal.ZERO);
    }

}
