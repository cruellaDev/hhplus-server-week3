package com.io.hhplus.concert.domain.concert.model;

import com.io.hhplus.concert.common.enums.ConcertStatus;

public record Concert(
        Long concertId,
        String concertName,
        String artistName,
        ConcertStatus concertStatus
) {
    public static Concert create(Long concertId, String concertName, String artistName, ConcertStatus concertStatus) {
        return new Concert(concertId, concertName, artistName, concertStatus);
    }

    public static Concert noContents() {
        return create(-1L, "N/A", "N/A",  ConcertStatus.NOT_AVAILABLE);
    }

    // insert 시 id는 null 이어야 하기 때문에 select, update 시에만 사용.
    public static boolean isAvailableConcertId(Long concertId) {
        return concertId != null && concertId.compareTo(0L) > 0;
    }

    public static boolean isAvailableConcertName(String concertName) {
        return concertName != null && !concertName.isBlank() && !concertName.equals("N/A");
    }

    public static boolean isAvailableStatus(ConcertStatus concertStatus) {
        return concertStatus != null && concertStatus.equals(ConcertStatus.AVAILABLE);
    }
}
