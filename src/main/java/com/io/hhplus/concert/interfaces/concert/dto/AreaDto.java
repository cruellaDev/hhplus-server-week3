package com.io.hhplus.concert.interfaces.concert.dto;

import com.io.hhplus.concert.application.concert.dto.AreaServiceResponse;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

public class AreaDto {

    @Builder
    public static class Response {
        private List<AreaResponse> areas;

        public static AreaDto.Response from(List<AreaServiceResponse> areas) {
            List<AreaDto.AreaResponse> areaResponses = areas
                    .stream()
                    .map(area -> AreaResponse.builder()
                            .areaId(area.areaId())
                            .concertId(area.concertId())
                            .performanceId(area.performanceId())
                            .areaName(area.areaName())
                            .seatPrice(area.seatPrice())
                            .seatCapacity(area.seatCapacity())
                            .build())
                    .toList();
            return AreaDto.Response.builder()
                    .areas(areaResponses)
                    .build();
        }
    }

    @Builder
    public static class AreaResponse {
        private Long areaId;
        private Long concertId;
        private Long performanceId;
        private String areaName;
        private BigDecimal seatPrice;
        private Long seatCapacity;
    }
}
