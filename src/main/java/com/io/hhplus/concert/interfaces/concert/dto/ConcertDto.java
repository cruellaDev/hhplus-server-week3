package com.io.hhplus.concert.interfaces.concert.dto;

import com.io.hhplus.concert.common.enums.ConcertStatus;
import com.io.hhplus.concert.domain.concert.ConcertCommand;
import com.io.hhplus.concert.domain.concert.model.Concert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

public class ConcertDto {

    @Data
    public static class RegisterRequest {
        private String concertName;
        private String artistName;
        private Date bookBeginAt;
        private Date bookEndAt;

        public ConcertCommand.RegisterConcertCommand toCommand() {
            return ConcertCommand.RegisterConcertCommand.builder()
                    .concertName(this.concertName)
                    .artistName(this.artistName)
                    .bookBeginAt(this.bookBeginAt)
                    .bookEndAt(this.bookEndAt)
                    .build();
        }
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SingleResponse {
        private ConcertResponse concert;

        public static ConcertDto.SingleResponse from(final Concert concert) {
            ConcertDto.ConcertResponse concertResponse = ConcertDto.ConcertResponse.builder()
                            .concertId(concert.concertId())
                            .concertName(concert.concertName())
                            .artistName(concert.artistName())
                            .concertStatus(concert.concertStatus())
                            .build();
            return ConcertDto.SingleResponse.builder()
                    .concert(concertResponse)
                    .build();
        }
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListResponse {
        private List<ConcertResponse> concerts;

        public static ConcertDto.ListResponse from(final List<Concert> concerts) {
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
            return ConcertDto.ListResponse.builder()
                    .concerts(concertResponses)
                    .build();
        }
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConcertResponse {
        private Long concertId;
        private String concertName;
        private String artistName;
        private ConcertStatus concertStatus;
    }

}
