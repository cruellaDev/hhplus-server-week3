package com.io.hhplus.concert.domain.queue.dto;

import com.io.hhplus.concert.common.enums.QueueStatus;
import com.io.hhplus.concert.domain.queue.model.QueueToken;
import lombok.Builder;

import java.util.UUID;

@Builder
public record BankCounterQueueTokenInfo(
    Long customerId,
    UUID queueToken,
    QueueStatus queueStatus,
    Long waitingCountAhead,
    Long waitingCountBehind
) {
    public static BankCounterQueueTokenInfo of(QueueToken queueToken, Long waitingCountAhead, Long waitingCountBehind) {
        return BankCounterQueueTokenInfo.builder()
                .customerId(queueToken.customerId())
                .queueToken(queueToken.queueToken())
                .queueStatus(queueToken.queueStatus())
                .waitingCountAhead(waitingCountAhead)
                .waitingCountBehind(waitingCountBehind)
                .build();
    }
}
