package com.io.hhplus.concert.interfaces.concert.dto;

import com.io.hhplus.concert.domain.concert.ConcertCommand;
import com.io.hhplus.concert.domain.concert.dto.ReservationInfo;
import com.io.hhplus.concert.domain.concert.model.Reservation;
import com.io.hhplus.concert.domain.concert.model.Ticket;
import lombok.Builder;

import java.util.List;

public class ReservationDto {

    public static class ReserveSeatsRequest {
        private Long customerId;
        private Long concertId;
        private Long concertScheduleId;
        private Long concertSeatId;
        private List<String> seatNumbers;

        public ConcertCommand.ReserveSeatsCommand toCommand() {
            return ConcertCommand.ReserveSeatsCommand.builder()
                    .customerId(this.customerId)
                    .concertId(this.concertId)
                    .concertScheduleId(this.concertScheduleId)
                    .concertSeatId(this.concertSeatId)
                    .seatNumbers(this.seatNumbers)
                    .build();
        }
    }

    @Builder
    public static class ReserveSeatsResponse {
        private Reservation reservation;
        private List<Ticket> tickets;

        public static ReservationDto.ReserveSeatsResponse from(ReservationInfo reservationInfo) {
            return ReserveSeatsResponse.builder()
                    .reservation(reservationInfo.getReservation())
                    .tickets(reservationInfo.getTickets())
                    .build();
        }
    }

}
