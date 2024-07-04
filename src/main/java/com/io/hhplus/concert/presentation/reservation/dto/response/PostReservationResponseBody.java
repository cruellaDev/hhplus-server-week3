package com.io.hhplus.concert.presentation.reservation.dto.response;

public record PostReservationResponseBody(
        Long customerId,
        Long reservationId,
        String reserverName,
        String reservationStatus
) {
}
