package com.io.hhplus.concert.interfaces.payment.dto;

import com.io.hhplus.concert.domain.concert.model.Reservation;
import com.io.hhplus.concert.domain.concert.model.Ticket;
import com.io.hhplus.concert.domain.payment.PaymentCommand;
import com.io.hhplus.concert.domain.payment.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class PaymentDto {

    @Data
    public static class CheckoutPaymentRequest {
        private Long customerId;
        private Long reservationId;
        private BigDecimal payAmount;

        public PaymentCommand.PayCommand toCommand(UUID token) {
            return PaymentCommand.PayCommand.builder()
                    .token(token)
                    .customerId(this.customerId)
                    .reservationId(this.reservationId)
                    .payAmount(this.payAmount)
                    .build();
        }
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckoutPaymentResponse {
        private Payment payment;
//        private Reservation reservation;
//        private List<Ticket> tickets;

        public static PaymentDto.CheckoutPaymentResponse from(Payment payment) {
            return CheckoutPaymentResponse.builder()
                    .payment(payment)
                    .build();
        }
    }
}
