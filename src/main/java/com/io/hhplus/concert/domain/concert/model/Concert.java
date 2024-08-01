package com.io.hhplus.concert.domain.concert.model;

import com.io.hhplus.concert.common.enums.ConcertStatus;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.common.utils.DateUtils;
import lombok.Builder;

import java.util.Date;

@Builder
public record Concert(
        Long concertId,
        String concertName,
        String artistName,
        Date bookBeginAt,
        Date bookEndAt,
        ConcertStatus concertStatus,
        Boolean isReceiveOnline,
        Boolean isReceiveOnSite,
        Boolean isReceiveByPost,
        Date createdAt,
        Date modifiedAt,
        Date deletedAt
) {
    public boolean isAbleToBook() {
        Date currentDate = DateUtils.getSysDate();
        return this.bookBeginAt.before(currentDate) && this.bookEndAt.after(currentDate);
    }

    public boolean isAvailableConcertStatus() {
        return this.concertStatus.isAvailable();
    }

    public boolean isNotDeleted() {
        return this.deletedAt == null;
    }

    public void checkValid() {
        if (isAbleToBook() && isAvailableConcertStatus() && isNotDeleted()) {
            return;
        }
        throw new CustomException(ResponseMessage.CONCERT_INVALID);
    }
}
