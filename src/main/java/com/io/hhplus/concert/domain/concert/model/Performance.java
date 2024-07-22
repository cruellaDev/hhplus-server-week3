package com.io.hhplus.concert.domain.concert.model;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Date;

@Builder
public record Performance(
        Long performanceId,
        Long concertId,
        BigDecimal performancePrice,
        Integer capacityLimit,
        Date performedAt,
        Date createdAt,
        Date modifiedAt,
        Date deletedAt
) {
    public boolean isToBePerformed(Date currentDate) {
        return this.performedAt().after(currentDate);
    }

    public boolean isNotDeleted() {
        return this.deletedAt == null;
    }
}
