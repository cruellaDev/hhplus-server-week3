package com.io.hhplus.concert.application.concert.dto;

import com.io.hhplus.concert.common.enums.ConcertStatus;
import com.io.hhplus.concert.domain.concert.model.Concert;
import lombok.Builder;

@Builder
public record ConcertServiceResponse(
        Long concertId,
        String concertName,
        String artistName,
        ConcertStatus concertStatus
) {
    public static ConcertServiceResponse from(Concert concert) {
        return ConcertServiceResponse.builder()
                .concertId(concert.concertId())
                .concertName(concert.concertName())
                .artistName(concert.artistName())
                .concertStatus(concert.concertStatus())
                .build();
    }
}
