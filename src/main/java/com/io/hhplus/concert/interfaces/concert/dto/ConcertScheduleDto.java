package com.io.hhplus.concert.interfaces.concert.dto;

import com.io.hhplus.concert.domain.concert.ConcertCommand;
import com.io.hhplus.concert.domain.concert.model.ConcertSchedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

public class ConcertScheduleDto {

    @Data
    public static class RegisterRequest {
        private Long concertId;
        private Date performedAt;

        public ConcertCommand.RegisterConcertScheduleCommand toCommand() {
            return ConcertCommand.RegisterConcertScheduleCommand.builder()
                    .concertId(this.concertId)
                    .performedAt(this.performedAt)
                    .build();
        }
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SingleResponse {
        private ConcertScheduleDto.ConcertScheduleResponse concertSchedule;

        public static ConcertScheduleDto.SingleResponse from(final ConcertSchedule concertSchedule) {
            ConcertScheduleDto.ConcertScheduleResponse concertScheduleResponse = ConcertScheduleDto.ConcertScheduleResponse.builder()
                    .concertId(concertSchedule.concertId())
                    .concertScheduleId(concertSchedule.concertScheduleId())
                    .performedAt(concertSchedule.performedAt())
                    .build();
            return SingleResponse.builder()
                    .concertSchedule(concertScheduleResponse)
                    .build();
        }
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListResponse {
        private List<ConcertScheduleDto.ConcertScheduleResponse> concertSchedules;

        public static ConcertScheduleDto.ListResponse from(final List<ConcertSchedule> concertSchedules) {
            List<ConcertScheduleDto.ConcertScheduleResponse> concertScheduleResponses = concertSchedules
                    .stream()
                    .map(concertSchedule
                            -> ConcertScheduleResponse.builder()
                            .concertId(concertSchedule.concertId())
                            .concertScheduleId(concertSchedule.concertScheduleId())
                            .performedAt(concertSchedule.performedAt())
                            .build())
                    .toList();
            return ConcertScheduleDto.ListResponse.builder()
                    .concertSchedules(concertScheduleResponses)
                    .build();
        }
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConcertScheduleResponse {
        private Long concertScheduleId;
        private Long concertId;
        private Date performedAt;
    }
}
