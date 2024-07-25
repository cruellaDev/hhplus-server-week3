package com.io.hhplus.concert.application.customer.dto;

import com.io.hhplus.concert.domain.customer.model.Customer;
import com.io.hhplus.concert.domain.customer.model.CustomerPointHistory;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Date;

@Builder
public record ChargeCustomerPointServiceResponse(
        Long customerId,
        BigDecimal pointBalance,
        BigDecimal chargeAmount,
        Date chargedAt
) {
    public static ChargeCustomerPointServiceResponse of(Customer customer, CustomerPointHistory customerPointHistory) {
        return ChargeCustomerPointServiceResponse.builder()
                .customerId(customer.customerId())
                .pointBalance(customer.pointBalance())
                .chargeAmount(customerPointHistory.pointAmount())
                .chargedAt(customerPointHistory.createdAt())
                .build();
    }
}
