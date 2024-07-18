package com.io.hhplus.concert.interfaces.customer.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class CustomerPointRequest {
    @NotNull
    private final Long customerId;

    @NotNull
    private final BigDecimal pointAmount;
}
