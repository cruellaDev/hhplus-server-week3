package com.io.hhplus.concert.interfaces.reservation.dto.request;

public record PostReserverRequestBody(
        Long customerId,
        String reserverName
) {
}
