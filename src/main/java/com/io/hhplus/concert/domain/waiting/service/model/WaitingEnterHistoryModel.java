package com.io.hhplus.concert.domain.waiting.service.model;

import java.util.Date;

public record WaitingEnterHistoryModel(
        Long waitingEnterHistoryId,
        Long waitingId,
        Date createdAt
) {
    public static WaitingEnterHistoryModel create(Long waitingEnterHistoryId, Long waitingId, Date createdAt) {
        return new WaitingEnterHistoryModel(waitingEnterHistoryId, waitingId, createdAt);
    }

    public static WaitingEnterHistoryModel noContents() {
        return create(-1L, -1L, null);
    }
}
