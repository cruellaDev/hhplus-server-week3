package com.io.hhplus.concert.application.concert.dto;

import com.io.hhplus.concert.common.enums.SeatStatus;
import com.io.hhplus.concert.domain.concert.model.Ticket;
import lombok.Builder;

@Builder
public record HeldSeatServiceResponse(
        Long reservationId,
        Long ticketId,
        Long concertId,
        Long performanceId,
        Long areaId,
        String seatNumber,
        SeatStatus seatStatus
) {
    public static HeldSeatServiceResponse from(Ticket ticket) {
        return HeldSeatServiceResponse.builder()
                .reservationId(ticket.reservationId())
                .ticketId(ticket.ticketId())
                .concertId(ticket.concertId())
                .performanceId(ticket.performanceId())
                .areaId(ticket.areaId())
                .seatNumber(ticket.seatNumber())
                .seatStatus(SeatStatus.WAITING_FOR_RESERVATION)
                .build();
    }
}
