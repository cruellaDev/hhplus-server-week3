package com.io.hhplus.concert.domain.concert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ConcertCommand {

    /**
     * 콘서트 등록 Command
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RegisterConcertCommand {
        private String concertName;
        private String artistName;
        private Date bookBeginAt;
        private Date bookEndAt;
    }

    /**
     * 콘서트 일정 등록 Command
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RegisterConcertScheduleCommand {
        private Long concertId;
        private Date performedAt;
    }

    /**
     * 콘서트 좌석 등록 Command
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RegisterConcertSeatCommand {
        private Long concertId;
        private Long concertScheduleId;
        private BigDecimal seatPrice;
        private Long seatCapacity;
    }

    /**
     * 콘서트 승인 Command
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ApproveConcertSeatCommand {
        private Long concertId;
    }

    /**
     * 좌석 예약 Command
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReserveSeatsCommand {
        private Long customerId;
        private String bookerName;
        private Long concertId;
        private Long concertScheduleId;
        private Long concertSeatId;
        private List<String> seatNumbers;
    }

    /**
     * 좌석 예약 Command
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ConfirmReservationCommand {
        private Long customerId;
        private Long reservationId;
    }

}
