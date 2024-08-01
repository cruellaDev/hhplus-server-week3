package com.io.hhplus.concert.common.enums;

public enum QueueStatus {
    ACTIVE,
    WAITING,
    EXPIRED;

    public boolean isActive() {
        return ACTIVE.equals(this);
    }

    public boolean isExpired() {
        return EXPIRED.equals(this);
    }
}
