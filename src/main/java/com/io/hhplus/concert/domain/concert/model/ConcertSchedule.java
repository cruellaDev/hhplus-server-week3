package com.io.hhplus.concert.domain.concert.model;

import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.common.utils.DateUtils;
import com.io.hhplus.concert.domain.concert.ConcertCommand;
import lombok.Builder;

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
    public static ConcertSchedule create() {
        return ConcertSchedule.builder().build();
    }

    public ConcertSchedule register(ConcertCommand.RegisterConcertScheduleCommand command) {
        if (command.getPerformedAt().before(DateUtils.getSysDate())) {
            throw new CustomException(ResponseMessage.CONCERT_SCHEDULE_INVALID, "예매 종료 일시는 현재 일시보다 이후여야 합니다.");
        }
        return ConcertSchedule.builder()
                .concertId(command.getConcertId())
                .performedAt(command.getPerformedAt())
                .build();
    }

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
