package com.io.hhplus.concert.interfaces.concert.dto;

import com.io.hhplus.concert.common.enums.ConcertStatus;
import com.io.hhplus.concert.domain.concert.model.Concert;
import lombok.Builder;

import java.util.List;

public class ConcertDto {

    @Builder
    public static class Response {
        private List<ConcertResponse> concerts;

        public static ConcertDto.Response from(List<Concert> concerts) {
            List<ConcertDto.ConcertResponse> concertResponses = concerts
                    .stream()
                    .map(concert
                            -> ConcertDto.ConcertResponse.builder()
                                .concertId(concert.concertId())
                                .concertName(concert.concertName())
                                .artistName(concert.artistName())
                                .concertStatus(concert.concertStatus())
                                .build())
                    .toList();
            return ConcertDto.Response.builder()
                    .concerts(concertResponses)
                    .build();
        }
    }

    @Builder
    public static class ConcertResponse {
        private Long concertId;
        private String concertName;
        private String artistName;
        private ConcertStatus concertStatus;
    }

}
