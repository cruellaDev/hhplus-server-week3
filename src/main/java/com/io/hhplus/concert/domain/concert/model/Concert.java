package com.io.hhplus.concert.domain.concert.model;

import com.io.hhplus.concert.common.enums.ConcertStatus;
import lombok.Builder;

import java.util.Date;

@Builder
public record Concert(
        Long concertId,
        String concertName,
        String artistName,
        Date saleBeginAt,
        Date saleEndAt,
        ConcertStatus concertStatus,
        Boolean isReceiveOnline,
        Boolean isReceiveOnSite,
        Boolean isReceiveOnPost,
        Date createdAt,
        Date modifiedAt,
        Date deletedAt
) {
    public boolean isOnSale(Date currentDate) {
        return this.saleBeginAt.before(currentDate) && this.saleEndAt.after(currentDate);
    }

    public boolean isAvailableConcertStatus() {
        return ConcertStatus.AVAILABLE.equals(this.concertStatus);
    }

    public boolean isNotDeleted() {
        return this.deletedAt == null;
    }
}
