package com.io.hhplus.concert.domain.concert.model;

import com.io.hhplus.concert.common.enums.SeatStatus;
import lombok.Builder;

@Builder
public record Seat(
        Long concertId,
        Long performanceId,
        Long areaId,
        String seatNumber,
        SeatStatus seatStatus
) {
    public boolean isAvailableStatus() {
        return this.seatStatus.isAvailable();
    }

    public static Seat createAvailableSeat(Area area, Long number) {
        return Seat.builder()
                .concertId(area.concertId())
                .performanceId(area.performanceId())
                .areaId(area.areaId())
                .seatNumber(area.areaName() + number)
                .seatStatus(SeatStatus.AVAILABLE)
                .build();
    }

    public static Seat createNotAvailableSeat(Area area, Long number) {
        return Seat.builder()
                .concertId(area.concertId())
                .performanceId(area.performanceId())
                .areaId(area.areaId())
                .seatNumber(area.areaName() + number)
                .seatStatus(SeatStatus.NOT_AVAILABLE)
                .build();
    }
}
