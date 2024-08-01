package com.io.hhplus.concert.domain.concert.dto;

import com.io.hhplus.concert.common.enums.SeatStatus;
import com.io.hhplus.concert.domain.concert.model.ConcertSeat;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AvailableSeatInfo(
        Long concertId,
        Long concertScheduleId,
        Long concertSeatId,
        BigDecimal seatPrice,
        Long seatCapacity,
        String seatNumber
) {
    public static AvailableSeatInfo of(ConcertSeat concertSeat, Long seatSequence) {
        return AvailableSeatInfo.builder()
                .concertId(concertSeat.concertId())
                .concertScheduleId(concertSeat.concertScheduleId())
                .concertSeatId(concertSeat.concertSeatId())
                .seatPrice(concertSeat.seatPrice())
                .seatCapacity(concertSeat.seatCapacity())
                .seatNumber(String.valueOf(seatSequence))
                .build();
    }
}
