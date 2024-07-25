package com.io.hhplus.concert.domain.concert.model;

import com.io.hhplus.concert.common.enums.SeatStatus;
import lombok.Builder;

import java.util.Date;

@Builder
public record Seat(
        Long seatId,
        Long performanceId,
        Long concertId,
        String seatNo,
        SeatStatus seatStatus,
        Date createdAt,
        Date modifiedAt,
        Date deletedAt
) {
    public boolean isAvailableStatus() {
        return this.seatStatus.isAvailable();
    }
    public boolean isNotDeleted() {
        return this.deletedAt == null;
    }
}
