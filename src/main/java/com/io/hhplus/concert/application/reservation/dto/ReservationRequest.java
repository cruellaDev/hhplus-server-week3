package com.io.hhplus.concert.application.reservation.dto;

import com.io.hhplus.concert.common.enums.ReceiveMethod;
import com.io.hhplus.concert.common.enums.ReservationStatus;
import com.io.hhplus.concert.domain.customer.model.Customer;
import com.io.hhplus.concert.domain.reservation.model.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class ReservationRequest {
    private final Reserver reserver;
    private final Receiver receiver;
    private final Concert concert;
    private final Performance performance;
    private final List<Seat> seats;

    @Getter
    @AllArgsConstructor
    @ToString
    public static class Reserver {
        private Long customerId;
        private String reserverName;
    }

    @Getter
    @AllArgsConstructor
    @ToString
    public static class Receiver {
        private ReceiveMethod receiveMethod;
        private String receiverName;
        private String receiverPostcode;
        private String receiverBaseAddress;
        private String receiverDetailAddress;
    }

    @Getter
    @AllArgsConstructor
    @ToString
    public static class Concert {
        private Long concertId;
        private String concertName;
    }

    @Getter
    @AllArgsConstructor
    @ToString
    public static class Performance {
        private Long performanceId;
        private Date performedAt;
        private BigDecimal price;
    }

    @Getter
    @AllArgsConstructor
    @ToString
    public static class Seat {
        private Long seatId;
        private String seatNo;
    }

    public static Reservation makeReservationOf(Customer customer, ReservationRequest.Reserver reserver, ReservationRequest.Receiver receiver) {
        return Reservation.create(
                null,
                customer.customerId(),
                reserver.getReserverName(),
                ReservationStatus.REQUESTED,
                new Date(),
                receiver.getReceiveMethod(),
                receiver.getReceiverName(),
                receiver.getReceiverPostcode(),
                receiver.getReceiverBaseAddress(),
                receiver.getReceiverDetailAddress());
    }
}
