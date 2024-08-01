package com.io.hhplus.concert.interfaces.concert.dto;

import com.io.hhplus.concert.domain.concert.dto.AvailableSeatInfo;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

public class ConcertSeatDto {

    @Builder
    public static class Response {
        private List<SeatResponse> seats;

        public static ConcertSeatDto.Response from(List<AvailableSeatInfo> seats) {
            List<ConcertSeatDto.SeatResponse> seatResponses = seats
                    .stream()
                    .map(seat -> SeatResponse.builder()
                            .concertId(seat.concertId())
                            .concertScheduleId(seat.concertScheduleId())
                            .concertSeatId(seat.concertSeatId())
                            .seatPrice(seat.seatPrice())
                            .seatCapacity(seat.seatCapacity())
                            .seatNumber(seat.seatNumber())
                            .build())
                    .toList();
            return ConcertSeatDto.Response.builder()
                    .seats(seatResponses)
                    .build();
        }
    }

    @Builder
    public static class SeatResponse {
        private Long concertId;
        private Long concertScheduleId;
        private Long concertSeatId;
        private BigDecimal seatPrice;
        private Long seatCapacity;
        private String seatNumber;
    }
}
