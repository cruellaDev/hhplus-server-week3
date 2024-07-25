package com.io.hhplus.concert.common.enums;

public enum ConcertStatus {
    WAITING,
    AVAILABLE,
    NOT_AVAILABLE,
    CLOSED;

    public boolean isAvailable() {
        return AVAILABLE.equals(this);
    }
}
