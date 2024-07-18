package com.io.hhplus.concert.application.reservation.dto;

import com.io.hhplus.concert.common.enums.PayMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class PaymentInfo {
    private final PayMethod payMethod;
    private final BigDecimal payAmount;
}
