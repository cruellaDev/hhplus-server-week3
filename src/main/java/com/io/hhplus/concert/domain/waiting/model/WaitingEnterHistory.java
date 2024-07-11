package com.io.hhplus.concert.domain.waiting.model;

import java.util.Date;

public record WaitingEnterHistory(
        Long waitingEnterHistoryId,
        Long waitingId,
        Date createdAt
) {
    public static WaitingEnterHistory create(Long waitingEnterHistoryId, Long waitingId, Date createdAt) {
        return new WaitingEnterHistory(waitingEnterHistoryId, waitingId, createdAt);
    }
}
