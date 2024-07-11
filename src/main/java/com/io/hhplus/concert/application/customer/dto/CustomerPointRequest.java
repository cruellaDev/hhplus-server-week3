package com.io.hhplus.concert.application.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class CustomerPointRequest {
    private final Long customerId;
    private final BigDecimal pointAmount;
}
