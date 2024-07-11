package com.io.hhplus.concert.domain.waiting.model;

import com.io.hhplus.concert.common.enums.WaitingStatus;

import java.util.Date;

public record WaitingQueue(
        Long waitingId,
        Long customerId,
        String token,
        WaitingStatus waitingStatus,
        Date createdAt,
        Date deletedAt
) {
    public static WaitingQueue create(Long waitingId, Long customerId, String token, WaitingStatus waitingStatus, Date createdAt, Date deletedAt) {
        return new WaitingQueue(waitingId, customerId, token, waitingStatus, createdAt, deletedAt);
    }

    public static WaitingQueue noContents() {
        return create(-1L, -1L, null, null, null, null);
    }

    public static boolean isAvailableWaitingId(Long waitingId) {
        return waitingId != null && waitingId.compareTo(0L) > 0;
    }

    public static boolean isAvailableCustomerId(Long customerId) {
        return customerId != null && customerId.compareTo(0L) > 0;
    }

    public static boolean isAvailableToken(String token) {
        return token != null && !token.isBlank();
    }

    public static boolean isAvailableWaitingStatus(WaitingStatus waitingStatus) {
        return waitingStatus != null && waitingStatus.equals(WaitingStatus.ACTIVE);
    }

    public static boolean isInActiveDuration(Long seconds, Long targetSeconds) {
        return seconds != null && targetSeconds != null && seconds.compareTo(targetSeconds) > 0;
    }
}
