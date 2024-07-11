package com.io.hhplus.concert.interfaces.waiting.dto.response;

public record PostWaitingQueueResponseBody(
        Long customerId,
        Long waitingNumber,
        String status,
        Integer remainingTime
) {
}
