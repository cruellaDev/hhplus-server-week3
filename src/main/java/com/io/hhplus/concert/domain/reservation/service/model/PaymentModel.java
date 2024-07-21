package com.io.hhplus.concert.domain.reservation.service.model;

import com.io.hhplus.concert.common.enums.PayMethod;

import java.math.BigDecimal;

public record PaymentModel(
        Long paymentId,
        Long reservationId,
        PayMethod payMethod,
        BigDecimal payAmount,
        BigDecimal refundableAmount,
        BigDecimal refundAmount
) {
    public static PaymentModel create(Long paymentId, Long reservationId, PayMethod payMethod, BigDecimal payAmount, BigDecimal refundableAmount, BigDecimal refundAmount) {
        return new PaymentModel(paymentId, reservationId, payMethod, payAmount, refundableAmount, refundAmount);
    }

    public static PaymentModel noContents() {
        return create(-1L, -1L, null, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }
}
