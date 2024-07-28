package com.io.hhplus.concert.application.concert.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class HoldSeatServiceRequest {

    private Long customerId;
    private Long concertId;
    private Long performanceId;
    private Long areaId;
    private List<SeatRequest> seats;

    @Builder
    @Getter
    public static class SeatRequest {
        private String seatNumber;
    }
}
