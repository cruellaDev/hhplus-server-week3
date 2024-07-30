package com.io.hhplus.concert.domain.concert.model;

import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Date;

@Builder
public record Area(
        Long areaId,
        Long performanceId,
        Long concertId,
        String areaName,
        BigDecimal seatPrice,
        Long seatCapacity,
        Date createdAt,
        Date modifiedAt,
        Date deletedAt
) {
    public boolean isNotDeleted() {
        return deletedAt == null;
    }

    public void validate() {
        if (isNotDeleted()) {
            return;
        }
        throw new CustomException(ResponseMessage.AREA_INVALID);
    }
}
