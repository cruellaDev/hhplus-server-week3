package com.io.hhplus.concert.application.reservation.dto;

import com.io.hhplus.concert.domain.reservation.service.model.ReservationModel;
import com.io.hhplus.concert.domain.reservation.service.model.TicketModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ReservationResultInfoWithTickets {
    private final ReservationModel reservation;
    private final List<TicketModel> tickets;
}
