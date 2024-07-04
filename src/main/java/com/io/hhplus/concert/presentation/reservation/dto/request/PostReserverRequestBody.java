package com.io.hhplus.concert.presentation.reservation.dto.request;

public record PostReserverRequestBody(
        Long customerId,
        String reserverName
) {
}
