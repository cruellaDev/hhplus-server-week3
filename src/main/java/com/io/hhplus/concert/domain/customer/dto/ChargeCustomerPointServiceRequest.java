package com.io.hhplus.concert.domain.customer.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class ChargeCustomerPointServiceRequest {
    private Long customerId;
    private BigDecimal amount;
}
