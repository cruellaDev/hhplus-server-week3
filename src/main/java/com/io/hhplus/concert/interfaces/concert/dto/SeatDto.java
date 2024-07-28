package com.io.hhplus.concert.interfaces.concert.dto;

import com.io.hhplus.concert.common.enums.SeatStatus;
import com.io.hhplus.concert.application.concert.dto.SeatServiceResponse;
import lombok.Builder;

import java.util.List;

public class SeatDto {

    @Builder
    public static class Response {
        private List<SeatResponse> seats;

        public static SeatDto.Response from(List<SeatServiceResponse> seats) {
            List<SeatDto.SeatResponse> seatResponses = seats
                    .stream()
                    .map(seat -> SeatResponse.builder()
                            .performanceId(seat.performanceId())
                            .concertId(seat.concertId())
                            .areaId(seat.areaId())
                            .seatNumber(seat.seatNumber())
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
        private Long concertId;
        private Long performanceId;
        private Long areaId;
        private String seatNumber;
        private SeatStatus seatStatus;
    }
}
