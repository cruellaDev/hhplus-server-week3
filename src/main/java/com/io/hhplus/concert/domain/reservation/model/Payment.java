package com.io.hhplus.concert.domain.reservation.model;

import com.io.hhplus.concert.common.enums.PayMethod;

import java.math.BigDecimal;

public record Payment(
        Long paymentId,
        Long reservationId,
        PayMethod payMethod,
        BigDecimal payAmount,
        BigDecimal refundableAmount,
        BigDecimal refundAmount
) {
    public static Payment create(Long paymentId, Long reservationId, PayMethod payMethod, BigDecimal payAmount, BigDecimal refundableAmount, BigDecimal refundAmount) {
        return new Payment(paymentId, reservationId, payMethod, payAmount, refundableAmount, refundAmount);
    }

    public static boolean isAvailablePayMethod(PayMethod payMethod) {
        return payMethod != null;
    }

    public static boolean isAvailablePayAmount(BigDecimal payAmount) {
        return payAmount != null && payAmount.compareTo(BigDecimal.ZERO) >= 0;
    }

    public static boolean isEqualPayAmount(BigDecimal payAmount, BigDecimal targetAmount) {
        return payAmount != null && targetAmount != null && payAmount.compareTo(targetAmount) == 0;
    }
}
