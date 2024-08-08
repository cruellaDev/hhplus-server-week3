package com.io.hhplus.concert.domain.concert.dto;

import com.io.hhplus.concert.domain.concert.model.Reservation;
import com.io.hhplus.concert.domain.concert.model.Ticket;
import lombok.Builder;

import java.util.List;

@Builder
public record ReservationInfo(
    Reservation reservation,
    List<Ticket> tickets
) {
    public static ReservationInfo from(Reservation reservation) {
        return ReservationInfo.builder()
                .reservation(reservation)
                .tickets(reservation.tickets())
                .build();
    }
    public static ReservationInfo of(Reservation reservation, List<Ticket> tickets) {
        return ReservationInfo.builder()
                .reservation(reservation)
                .tickets(tickets)
                .build();
    }
}
