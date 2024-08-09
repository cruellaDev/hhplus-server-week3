package com.io.hhplus.concert.domain.concert.model;

import com.io.hhplus.concert.common.GlobalConstants;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.common.utils.DateUtils;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Date;

@Builder
public record Ticket(
        Long ticketId,
        Long reservationId,
        Long concertId,
        Long concertScheduleId,
        Long concertSeatId,
        String concertName,
        String artistName,
        Date performedAt,
        String seatNumber,
        BigDecimal ticketPrice,
        Date reservedAt,
        Date publishedAt,
        Date receivedAt,
        Date cancelAcceptedAt,
        Date cancelApprovedAt,
        Date createdAt,
        Date modifiedAt,
        Date deletedAt
) {
    public static Ticket reserve(Concert concert, ConcertSchedule concertSchedule, ConcertSeat concertSeat, String seatNumber) {
        concert.checkValid();
        concertSchedule.checkValid();
        concertSeat.checkValid();

        return Ticket.builder()
                .concertId(concert.concertId())
                .concertScheduleId(concertSchedule.concertScheduleId())
                .concertSeatId(concertSeat.concertSeatId())
                .concertName(concert.concertName())
                .artistName(concert.artistName())
                .performedAt(concertSchedule.performedAt())
                .seatNumber(seatNumber)
                .ticketPrice(concertSeat.seatPrice())
                .build();
    }

    public boolean isAbleToConfirmReservation() {
        return this.reservedAt == null
                && DateUtils.calculateDuration(DateUtils.getSysDate(), this.createdAt) < GlobalConstants.MAX_DURATION_OF_ACTIVE_QUEUE_IN_SECONDS
                && this.deletedAt == null;
    }

    public Ticket confirmReservation(Reservation reservation) {
        if (!isAbleToConfirmReservation()) {
            throw new CustomException(ResponseMessage.INVALID, "예약이 가능한 상태가 아닙니다.");
        }
        Date currentDate = DateUtils.getSysDate();
        return Ticket.builder()
                .reservationId(reservation.reservationId())
                .concertId(this.concertId)
                .concertScheduleId(this.concertScheduleId)
                .concertSeatId(this.concertSeatId)
                .concertName(this.concertName)
                .artistName(this.artistName)
                .performedAt(this.performedAt)
                .seatNumber(this.seatNumber)
                .ticketPrice(this.ticketPrice)
                .reservedAt(currentDate)
                .publishedAt(this.publishedAt)
                .receivedAt(this.receivedAt)
                .build();
    }
}
