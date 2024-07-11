package com.io.hhplus.concert.domain.reservation.model;

import com.io.hhplus.concert.common.enums.ReceiveMethod;
import com.io.hhplus.concert.common.enums.ReservationStatus;

import java.util.Date;

public record Reservation(
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
    public static Reservation create(Long reservationId, Long customerId, String reserverName, ReservationStatus reservationStatus, Date reservationStatusChangedAt, ReceiveMethod receiveMethod, String receiverName, String receiverPostcode, String receiverBaseAddress, String receiverDetailAddress) {
        return new Reservation(reservationId, customerId, reserverName, reservationStatus, reservationStatusChangedAt, receiveMethod, receiverName, receiverPostcode, receiverBaseAddress, receiverDetailAddress);
    }

    public static Reservation noContents() {
        return create(-1L, -1L, "N/A", null, null, null, "N/A", "N/A", "N/A", "N/A");
    }

    public static Reservation changeStatus(Reservation reservation, ReservationStatus reservationStatus) {
        return create(reservation.reservationId, reservation.customerId(), reservation.reserverName, reservationStatus, new Date(), reservation.receiveMethod(), reservation.receiverName(), reservation.receiverPostcode, reservation.receiverBaseAddress, reservation.receiverDetailAddress());
    }

    public static boolean isAvailableReservationId(Long reservationId) {
        return reservationId != null && reservationId.compareTo(0L) > 0;
    }

    public static boolean isAvailableReserverName(String reserverName) {
        return reserverName != null && !reserverName.isBlank() && !reserverName.equals("N/A");
    }

    public static boolean isAvailableReceiverName(String receiverName) {
        return receiverName != null && !receiverName.isBlank() && !receiverName.equals("N/A");
    }

    public static boolean isAvailableReceiveMethod(ReceiveMethod receiveMethod) {
        return receiveMethod != null;
    }

    public static boolean isAvailableReservationStatus(ReservationStatus reservationStatus) {
        return reservationStatus != null;
    }

    public static boolean isAbleToPayReservationStatus(ReservationStatus reservationStatus) {
        return  isAvailableReservationStatus(reservationStatus) && reservationStatus.equals(ReservationStatus.REQUESTED);
    }

    public static boolean isInPayableDuration(Long seconds, Long targetSeconds) {
        return seconds != null && targetSeconds != null && seconds.compareTo(targetSeconds) > 0;
    }
}
