package com.io.hhplus.concert.application.customer.dto;

import com.io.hhplus.concert.domain.customer.model.Customer;
import com.io.hhplus.concert.domain.customer.model.CustomerPointHistory;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Date;

@Builder
public record UseCustomerPointServiceResponse(
        Long customerId,
        BigDecimal pointBalance,
        BigDecimal useAmount,
        Date usedAt
) {
    public static UseCustomerPointServiceResponse of(Customer customer, CustomerPointHistory customerPointHistory) {
        return UseCustomerPointServiceResponse.builder()
                .customerId(customer.customerId())
                .pointBalance(customer.pointBalance())
                .useAmount(customerPointHistory.pointAmount())
                .usedAt(customerPointHistory.createdAt())
                .build();
    }
}
