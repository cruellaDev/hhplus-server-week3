package com.io.hhplus.concert.domain.concert.model;

import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Date;

@Builder
public record ConcertSeat(
        Long concertSeatId,
        Long concertScheduleId,
        Long concertId,
        BigDecimal seatPrice,
        Long seatCapacity,
        Date createdAt,
        Date modifiedAt,
        Date deletedAt
) {
    public boolean hasEnoughSeats() {
        return this.seatCapacity != null && this.seatCapacity.compareTo(0L) > 0;
    }

    public boolean isNotDeleted() {
        return deletedAt == null;
    }

    public void checkValid() {
        if (hasEnoughSeats() && isNotDeleted()) {
            return;
        }
        throw new CustomException(ResponseMessage.CONCERT_SEAT_INVALID);
    }
}
