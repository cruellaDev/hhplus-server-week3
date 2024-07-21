package com.io.hhplus.concert.domain.waiting.service.model;

import com.io.hhplus.concert.common.enums.WaitingStatus;

import java.util.Date;

public record WaitingQueueModel(
        Long waitingId,
        Long customerId,
        String token,
        WaitingStatus waitingStatus,
        Date createdAt,
        Date deletedAt
) {
    public static WaitingQueueModel create(Long waitingId, Long customerId, String token, WaitingStatus waitingStatus, Date createdAt, Date deletedAt) {
        return new WaitingQueueModel(waitingId, customerId, token, waitingStatus, createdAt, deletedAt);
    }

    public static WaitingQueueModel noContents() {
        return create(-1L, -1L, null, null, null, null);
    }
}
