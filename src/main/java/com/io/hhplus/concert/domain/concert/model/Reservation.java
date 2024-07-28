package com.io.hhplus.concert.domain.concert.model;

import com.io.hhplus.concert.common.enums.ReceiveMethod;
import com.io.hhplus.concert.common.enums.ReservationStatus;
import lombok.Builder;

import java.util.Date;

@Builder
public record Reservation(
        Long reservationId,
        Long customerId,
        String reserverName,
        ReceiveMethod receiveMethod,
        String receiverName,
        String receiverPostcode,
        String receiverBaseAddress,
        String receiverDetailAddress,
        Date createdAt,
        Date modifiedAt,
        Date deletedAt
) {
    public static Reservation create(Long customerId) {
        return Reservation.builder()
                .customerId(customerId)
                .build();
    }
}
