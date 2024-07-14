package com.io.hhplus.concert.domain.customer.model;

import java.math.BigDecimal;

public record Customer(
        Long customerId,
        String customerName,
        BigDecimal pointBalance
) {
    public static Customer create(Long customerId, String customerName, BigDecimal pointBalance) {
        return new Customer(customerId, customerName, pointBalance);
    }

    public static Customer noContents() {
        return create(-1L, "N/A", BigDecimal.ZERO);
    }

    public static boolean isAvailableCustomerId(Long customerId) {
        return customerId != null && customerId.compareTo(0L) > 0;
    }

    public static boolean isAvailableCustomerName(String customerName) {
        return customerName != null && !customerName.isBlank() && !customerName.equals("N/A");
    }

    public static boolean isInsufficientPointBalance(BigDecimal pointBalance, BigDecimal targetAmount) {
        return pointBalance == null || pointBalance.compareTo(targetAmount) < 0;
    }
}
