package com.io.hhplus.concert.interfaces.concert.dto;

import com.io.hhplus.concert.domain.concert.ConcertCommand;
import com.io.hhplus.concert.domain.concert.dto.AvailableSeatInfo;
import com.io.hhplus.concert.domain.concert.model.ConcertSeat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

public class ConcertSeatDto {

    @Data
    public static class RegisterRequest {
        private Long concertId;
        private Long concertScheduleId;
        private BigDecimal seatPrice;
        private Long seatCapacity;

        public ConcertCommand.RegisterConcertSeatCommand toCommand() {
            return ConcertCommand.RegisterConcertSeatCommand.builder()
                    .concertId(this.concertId)
                    .concertScheduleId(this.concertScheduleId)
                    .seatPrice(this.seatPrice)
                    .seatCapacity(this.seatCapacity)
                    .build();
        }
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SingleResponse {
        private SeatResponse seat;

        public static ConcertSeatDto.SingleResponse from(ConcertSeat concertSeat) {
            ConcertSeatDto.SeatResponse seat = ConcertSeatDto.SeatResponse.builder()
                    .concertId(concertSeat.concertId())
                    .concertScheduleId(concertSeat.concertScheduleId())
                    .concertSeatId(concertSeat.concertSeatId())
                    .seatPrice(concertSeat.seatPrice())
                    .seatCapacity(concertSeat.seatCapacity())
                    .build();
            return SingleResponse.builder()
                    .seat(seat)
                    .build();
        }
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListResponse {
        private List<SeatResponse> seats;

        public static ConcertSeatDto.ListResponse from(List<AvailableSeatInfo> seats) {
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
            return ConcertSeatDto.ListResponse.builder()
                    .seats(seatResponses)
                    .build();
        }
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SeatResponse {
        private Long concertId;
        private Long concertScheduleId;
        private Long concertSeatId;
        private BigDecimal seatPrice;
        private Long seatCapacity;
        private String seatNumber;
    }
}
