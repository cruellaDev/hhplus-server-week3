package com.io.hhplus.concert.domain.concert.model;

import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.domain.concert.ConcertCommand;
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
    public static ConcertSeat create() {
        return ConcertSeat.builder().build();
    }

    public ConcertSeat register(ConcertCommand.RegisterConcertSeatCommand command) {
        if (command.getSeatPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new CustomException(ResponseMessage.CONCERT_SEAT_INVALID, "좌석 가격은 0 이상이어야 합니다");
        }
        if (command.getSeatCapacity().compareTo(0L) <= 0) {
            throw new CustomException(ResponseMessage.CONCERT_SEAT_INVALID, "좌석 인원은 0보다 커야 합니다");
        }
        return ConcertSeat.builder()
                .concertScheduleId(command.getConcertScheduleId())
                .concertId(command.getConcertId())
                .seatPrice(command.getSeatPrice())
                .seatCapacity(command.getSeatCapacity())
                .build();
    }

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
