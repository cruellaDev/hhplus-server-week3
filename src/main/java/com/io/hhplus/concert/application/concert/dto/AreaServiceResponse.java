package com.io.hhplus.concert.application.concert.dto;

import com.io.hhplus.concert.domain.concert.model.ConcertSeat;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AreaServiceResponse(
        Long areaId,
        Long performanceId,
        Long concertId,
        String areaName,
        BigDecimal seatPrice,
        Long seatCapacity
) {
    public static AreaServiceResponse from(ConcertSeat seat) {
        return AreaServiceResponse.builder()
                .areaId(seat.areaId())
                .performanceId(seat.performanceId())
                .concertId(seat.concertId())
                .areaName(seat.areaName())
                .seatPrice(seat.seatPrice())
                .build();
    }
}
