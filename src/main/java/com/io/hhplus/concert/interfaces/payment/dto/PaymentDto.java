package com.io.hhplus.concert.interfaces.payment.dto;

import com.io.hhplus.concert.application.payment.CompositeCommand;
import com.io.hhplus.concert.application.payment.dto.CheckoutPaymentResultAndConfirmedReservationInfo;
import com.io.hhplus.concert.domain.concert.model.Reservation;
import com.io.hhplus.concert.domain.concert.model.Ticket;
import com.io.hhplus.concert.domain.payment.model.Payment;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

public class PaymentDto {

    public static class CheckoutPaymentRequest {
        private Long customerId;
        private Long reservationId;
        private BigDecimal payAmount;

        public CompositeCommand.CheckoutPaymentCommand toCommand() {
            return CompositeCommand.CheckoutPaymentCommand.builder()
                    .customerId(this.customerId)
                    .reservationId(this.reservationId)
                    .payAmount(this.payAmount)
                    .build();
        }
    }

    @Builder
    public static class CheckoutPaymentResponse {
        private Payment payment;
        private Reservation reservation;
        private List<Ticket> tickets;

        public static PaymentDto.CheckoutPaymentResponse from(CheckoutPaymentResultAndConfirmedReservationInfo checkoutPaymentResultAndConfirmedReservationInfo) {
            return CheckoutPaymentResponse.builder()
                    .payment(checkoutPaymentResultAndConfirmedReservationInfo.getPayment())
                    .reservation(checkoutPaymentResultAndConfirmedReservationInfo.getReservation())
                    .tickets(checkoutPaymentResultAndConfirmedReservationInfo.getTickets())
                    .build();
        }
    }
}
