package com.io.hhplus.concert.presentation.waiting.dto.response;

public record PostWaitingQueueResponseBody(
        Long customerId,
        Long waitingNumber,
        String status,
        Integer remainingTime
) {
}
