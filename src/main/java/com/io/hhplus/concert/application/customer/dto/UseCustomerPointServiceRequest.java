package com.io.hhplus.concert.application.customer.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class UseCustomerPointServiceRequest {
    private Long customerId;
    private BigDecimal amount;
}
