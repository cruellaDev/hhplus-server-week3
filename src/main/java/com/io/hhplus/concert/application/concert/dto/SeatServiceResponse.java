package com.io.hhplus.concert.application.concert.dto;

import com.io.hhplus.concert.common.enums.SeatStatus;
import com.io.hhplus.concert.domain.concert.dto.AvailableSeatInfo;
import lombok.Builder;

@Builder
public record SeatServiceResponse(
        Long concertId,
        Long performanceId,
        Long areaId,
        String seatNumber,
        SeatStatus seatStatus
) {
    public static SeatServiceResponse from(AvailableSeatInfo seat) {
        return SeatServiceResponse.builder()
                .concertId(seat.concertId())
                .performanceId(seat.performanceId())
                .areaId(seat.areaId())
                .seatNumber(seat.seatNumber())
                .seatStatus(seat.seatStatus())
                .build();
    }
}
