package com.io.hhplus.concert.domain.concert.model;

import com.io.hhplus.concert.common.GlobalConstants;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.common.utils.DateUtils;
import com.io.hhplus.concert.domain.concert.ConcertCommand;
import lombok.Builder;

import java.util.Date;

@Builder
public record Reservation(
        Long reservationId,
        Long customerId,
        String bookerName,
        Date createdAt,
        Date modifiedAt,
        Date deletedAt
) {
    public static Reservation create() {
        return Reservation.builder().build();
    }

    public Reservation reserve(ConcertCommand.ReserveSeatsCommand command) {
        if (this.reservationId != null && this.reservationId.compareTo(0L) > 0) {
            throw new CustomException(ResponseMessage.ALREADY_RESERVED);
        }
        if (isNotDeleted() && DateUtils.calculateDuration(this.createdAt, DateUtils.getSysDate()) < GlobalConstants.MAX_DURATION_OF_ACTIVE_QUEUE_IN_SECONDS) {
            throw new CustomException(ResponseMessage.RESERVATION_TIMED_OUT);
        }

        return Reservation.builder()
                .customerId(command.getCustomerId())
                .bookerName(command.getBookerName())
                .build();
    }

    public boolean isNotDeleted() {
        return this.deletedAt == null;
    }
}
