package com.io.hhplus.concert.interfaces.concert.dto;

import com.io.hhplus.concert.domain.concert.model.ConcertSchedule;
import lombok.Builder;

import java.util.Date;
import java.util.List;

public class ConcertScheduleDto {

    @Builder
    public static class Response {
        private List<ConcertScheduleDto.PerformanceResponse> performances;

        public static ConcertScheduleDto.Response from(List<ConcertSchedule> performances) {
            List<ConcertScheduleDto.PerformanceResponse> performanceResponses = performances
                    .stream()
                    .map(performance
                            -> ConcertScheduleDto.PerformanceResponse.builder()
                            .concertScheduleId(performance.concertScheduleId())
                            .performedAt(performance.performedAt())
                            .build())
                    .toList();
            return ConcertScheduleDto.Response.builder()
                    .performances(performanceResponses)
                    .build();
        }
    }

    @Builder
    public static class PerformanceResponse {
        private Long concertScheduleId;
        private Long concertId;
        private Date performedAt;
    }
}
