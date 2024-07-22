package com.io.hhplus.concert.application.concert.dto;

import com.io.hhplus.concert.common.enums.SeatStatus;
import com.io.hhplus.concert.domain.concert.model.Seat;
import lombok.Builder;

@Builder
public record AvailableSeatServiceResponse (
        Long seatId,
        Long performanceId,
        Long concertId,
        String seatNo,
        SeatStatus seatStatus
) {
    public static AvailableSeatServiceResponse from(Seat seat) {
        return AvailableSeatServiceResponse.builder()
                .seatId(seat.seatId())
                .performanceId(seat.performanceId())
                .concertId(seat.concertId())
                .seatNo(seat.seatNo())
                .build();
    }
}
