package com.io.hhplus.concert.application.concert.dto;

import com.io.hhplus.concert.common.enums.SeatStatus;
import com.io.hhplus.concert.domain.concert.model.Ticket;
import lombok.Builder;

@Builder
public record HoldSeatServiceResponse(
        Long reservationId,
        Long concertId,
        Long performanceId,
        Long areaId,
        String seatNumber,
        SeatStatus seatStatus
) {
    public static HoldSeatServiceResponse from(Ticket ticket) {
        return HoldSeatServiceResponse.builder()
                .reservationId(ticket.reservationId())
                .concertId(ticket.concertId())
                .performanceId(ticket.performanceId())
                .areaId(ticket.areaId())
                .seatNumber(ticket.seatNumber())
                .seatStatus(SeatStatus.WAITING_FOR_RESERVATION)
                .build();
    }
}
