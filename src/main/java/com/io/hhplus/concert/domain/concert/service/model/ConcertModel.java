package com.io.hhplus.concert.domain.concert.service.model;

import com.io.hhplus.concert.common.enums.ConcertStatus;

public record ConcertModel(
        Long concertId,
        String concertName,
        String artistName,
        ConcertStatus concertStatus
) {
    public static ConcertModel create(Long concertId, String concertName, String artistName, ConcertStatus concertStatus) {
        return new ConcertModel(concertId, concertName, artistName, concertStatus);
    }

    public static ConcertModel noContents() {
        return create(-1L, "N/A", "N/A",  ConcertStatus.NOT_AVAILABLE);
    }

}
