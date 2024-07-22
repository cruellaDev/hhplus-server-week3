package com.io.hhplus.concert.interfaces.concert.dto;

import com.io.hhplus.concert.application.concert.dto.AvailablePerformanceServiceResponse;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class PerformanceDto {

    @Builder
    public static class Response {
        private List<PerformanceDto.PerformanceResponse> performances;

        public static PerformanceDto.Response from(List<AvailablePerformanceServiceResponse> performances) {
            List<PerformanceDto.PerformanceResponse> performanceResponses = performances
                    .stream()
                    .map(performance
                            -> PerformanceDto.PerformanceResponse.builder()
                            .performanceId(performance.performanceId())
                            .concertId(performance.concertId())
                            .performancePrice(performance.performancePrice())
                            .capacityLimit(performance.capacityLimit())
                            .performedAt(performance.performedAt())
                            .build())
                    .toList();
            return PerformanceDto.Response.builder()
                    .performances(performanceResponses)
                    .build();
        }
    }

    @Builder
    public static class PerformanceResponse {
        private Long performanceId;
        private Long concertId;
        private BigDecimal performancePrice;
        private Integer capacityLimit;
        private Date performedAt;
    }
}
