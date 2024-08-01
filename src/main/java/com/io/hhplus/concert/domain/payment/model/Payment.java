package com.io.hhplus.concert.domain.payment.model;

import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.domain.payment.PaymentCommand;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Date;

@Builder
public record Payment(
        Long paymentId,
        Long reservationId,
        BigDecimal payAmount,
        BigDecimal refundableAmount,
        BigDecimal refundAmount,
        Date createdAt,
        Date modifiedAt,
        Date deletedAt
) {
    public static Payment create() {
        return Payment.builder().build();
    }

    public boolean isValidPayAmount() {
        return this.payAmount != null && this.payAmount.compareTo(BigDecimal.ZERO) >= 0;
    }

    public Payment pay(PaymentCommand.CompletePaymentCommand command) {
        if(!isValidPayAmount()) throw new CustomException(ResponseMessage.PAYMENT_AMOUNT_INVALID);
        return Payment.builder()
                .reservationId(command.getReservationId())
                .payAmount(command.getPayAmount())
                .refundableAmount(command.getPayAmount())
                .refundAmount(BigDecimal.ZERO)
                .build();
    }
}
