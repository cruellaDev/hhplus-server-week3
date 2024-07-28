package com.io.hhplus.concert.application.concert.dto;

import com.io.hhplus.concert.domain.concert.model.Area;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AreaServiceResponse(
        Long areaId,
        Long performanceId,
        Long concertId,
        String areaName,
        BigDecimal seatPrice,
        Long seatCapacity
) {
    public static AreaServiceResponse from(Area area) {
        return AreaServiceResponse.builder()
                .areaId(area.areaId())
                .performanceId(area.performanceId())
                .concertId(area.concertId())
                .areaName(area.areaName())
                .seatPrice(area.seatPrice())
                .build();
    }
}
