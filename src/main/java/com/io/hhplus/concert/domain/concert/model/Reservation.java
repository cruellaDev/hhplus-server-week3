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
    @Builder
    public record ReceiveInfo(
            String reserverName,
            ReceiveMethod receiveMethod,
            String receiverName,
            String receiverPostcode,
            String receiverBaseAddress,
            String receiverDetailAddress
            ) {
    }

    public static Reservation create(Long customerId) {
        return Reservation.builder()
                .customerId(customerId)
                .build();
    }

    public boolean isNotDeleted() {
        return this.deletedAt == null;
    }

    public boolean isReceivedOnline() {
        return this.receiveMethod.isReceivedOnline();
    }

    public Reservation fillReceiveInfo(Reservation.ReceiveInfo receiveInfo) {
        return Reservation.builder()
                .reservationId(this.reservationId)
                .customerId(this.customerId)
                .reserverName(receiveInfo.reserverName)
                .receiveMethod(receiveInfo.receiveMethod())
                .receiverName(receiveInfo.receiverName)
                .receiverPostcode(receiveInfo.receiverPostcode)
                .receiverBaseAddress(receiveInfo.receiverBaseAddress)
                .receiverDetailAddress(receiveInfo.receiverDetailAddress)
                .build();
    }
}
