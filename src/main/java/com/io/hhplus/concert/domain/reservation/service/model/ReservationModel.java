package com.io.hhplus.concert.domain.reservation.service.model;

import com.io.hhplus.concert.common.enums.ReceiveMethod;
import com.io.hhplus.concert.common.enums.ReservationStatus;

import java.util.Date;

public record ReservationModel(
        Long reservationId,
        Long customerId,
        String reserverName,
        ReservationStatus reservationStatus,
        Date reservationStatusChangedAt,
        ReceiveMethod receiveMethod,
        String receiverName,
        String receiverPostcode,
        String receiverBaseAddress,
        String receiverDetailAddress
) {
    public static ReservationModel create(Long reservationId, Long customerId, String reserverName, ReservationStatus reservationStatus, Date reservationStatusChangedAt, ReceiveMethod receiveMethod, String receiverName, String receiverPostcode, String receiverBaseAddress, String receiverDetailAddress) {
        return new ReservationModel(reservationId, customerId, reserverName, reservationStatus, reservationStatusChangedAt, receiveMethod, receiverName, receiverPostcode, receiverBaseAddress, receiverDetailAddress);
    }

    public static ReservationModel noContents() {
        return create(-1L, -1L, "N/A", null, null, null, "N/A", "N/A", "N/A", "N/A");
    }

    public static ReservationModel changeStatus(ReservationModel reservationModel, ReservationStatus reservationStatus) {
        return create(reservationModel.reservationId, reservationModel.customerId(), reservationModel.reserverName, reservationStatus, new Date(), reservationModel.receiveMethod(), reservationModel.receiverName(), reservationModel.receiverPostcode, reservationModel.receiverBaseAddress, reservationModel.receiverDetailAddress());
    }

}
