package com.io.hhplus.concert.application.customer.dto;

import com.io.hhplus.concert.domain.customer.model.Customer;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CustomerPointBalanceServiceResponse(
        Long customerId,
        BigDecimal pointBalance
) {
    public static CustomerPointBalanceServiceResponse from(Customer customer) {
        return CustomerPointBalanceServiceResponse.builder()
                .customerId(customer.customerId())
                .pointBalance(customer.pointBalance())
                .build();
    }
}
