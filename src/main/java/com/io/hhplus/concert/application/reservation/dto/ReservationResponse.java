package com.io.hhplus.concert.application.reservation.dto;

import com.io.hhplus.concert.domain.reservation.model.Reservation;
import com.io.hhplus.concert.domain.reservation.model.Ticket;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReservationResponse {
    private final Reservation reservation;
    private final List<Ticket> tickets;
}
