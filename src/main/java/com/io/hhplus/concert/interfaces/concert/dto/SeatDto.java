package com.io.hhplus.concert.interfaces.concert.dto;

import com.io.hhplus.concert.common.enums.SeatStatus;
import com.io.hhplus.concert.application.concert.dto.AvailableSeatServiceResponse;
import lombok.Builder;

import java.util.List;

public class SeatDto {

    @Builder
    public static class Response {
        private List<SeatResponse> seats;

        public static SeatDto.Response from(List<AvailableSeatServiceResponse> seats) {
            List<SeatDto.SeatResponse> seatResponses = seats
                    .stream()
                    .map(seat -> SeatResponse.builder()
                            .seatId(seat.seatId())
                            .performanceId(seat.performanceId())
                            .concertId(seat.concertId())
                            .seatNo(seat.seatNo())
                            .seatStatus(seat.seatStatus())
                            .build())
                    .toList();
            return SeatDto.Response.builder()
                    .seats(seatResponses)
                    .build();
        }
    }

    @Builder
    public static class SeatResponse {
        private Long seatId;
        private Long performanceId;
        private Long concertId;
        private String seatNo;
        private SeatStatus seatStatus;
    }
}
