package com.io.hhplus.concert.application.payment.dto;

import com.io.hhplus.concert.domain.concert.dto.ReservationInfo;
import com.io.hhplus.concert.domain.concert.model.Reservation;
import com.io.hhplus.concert.domain.concert.model.Ticket;
import com.io.hhplus.concert.domain.payment.model.Payment;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CheckoutPaymentResultAndConfirmedReservationInfo {
    private Payment payment;
    private Reservation reservation;
    private List<Ticket> tickets;

    public static CheckoutPaymentResultAndConfirmedReservationInfo of(Payment payment, ReservationInfo reservationInfo) {
        return CheckoutPaymentResultAndConfirmedReservationInfo.builder()
                .payment(payment)
                .reservation(reservationInfo.reservation())
                .tickets(reservationInfo.tickets())
                .build();
    }
}
