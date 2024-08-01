package com.io.hhplus.concert.domain.concert.dto;

import com.io.hhplus.concert.domain.concert.model.Reservation;
import com.io.hhplus.concert.domain.concert.model.Ticket;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ReservationInfo {
    Reservation reservation;
    List<Ticket> tickets;

    public static ReservationInfo of(Reservation reservation, List<Ticket> tickets) {
        return ReservationInfo.builder()
                .reservation(reservation)
                .tickets(tickets)
                .build();
    }
}
