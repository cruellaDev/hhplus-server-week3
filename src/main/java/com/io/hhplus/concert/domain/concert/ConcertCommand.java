package com.io.hhplus.concert.domain.concert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class ConcertCommand {

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
