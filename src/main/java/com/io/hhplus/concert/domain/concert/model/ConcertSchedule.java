package com.io.hhplus.concert.domain.concert.model;

import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.common.utils.DateUtils;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Date;

@Builder
public record ConcertSchedule(
        Long concertScheduleId,
        Long concertId,
        Date performedAt,
        Date createdAt,
        Date modifiedAt,
        Date deletedAt
) {
    public boolean isToBePerformed() {
        return this.performedAt().after(DateUtils.getSysDate());
    }

    public boolean isNotDeleted() {
        return this.deletedAt == null;
    }

    public void checkValid() {
        if (isToBePerformed() && isNotDeleted()) {
            return;
        }
        throw new CustomException(ResponseMessage.CONCERT_SCHEDULE_INVALID);
    }
}
