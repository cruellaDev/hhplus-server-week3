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
        Long performanceId,
        Long areaId,
        String concertName,
        String artistName,
        Date performedAt,
        String areaName,
        String seatNumber,
        Boolean isReceiveOnline,
        Boolean isReceiveOnSite,
        Boolean isReceiveByPost,
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
    public static Ticket create(Reservation reservation, Concert concert, Performance performance, Area area, String seatNumber) {
        return Ticket.builder()
                .reservationId(reservation.reservationId())
                .concertId(concert.concertId())
                .performanceId(performance.performanceId())
                .areaId(area.areaId())
                .concertName(concert.concertName())
                .artistName(concert.artistName())
                .performedAt(performance.performedAt())
                .areaName(area.areaName())
                .seatNumber(seatNumber)
                .isReceiveOnline(concert.isReceiveOnline())
                .isReceiveOnSite(concert.isReceiveOnSite())
                .isReceiveByPost(concert.isReceiveByPost())
                .ticketPrice(area.seatPrice())
                .build();
    }

    public boolean isReservable() {
        return (this.reservedAt == null || this.cancelAcceptedAt == null)
                && DateUtils.calculateDuration(this.createdAt, DateUtils.getSysDate()) < GlobalConstants.MAX_DURATION_OF_ACTIVE_QUEUE_IN_SECONDS
                && this.deletedAt == null;
    }

    public Ticket confirmReservation(Reservation reservation) {
        if (!isReservable()) {
            throw new CustomException(ResponseMessage.INVALID, "예약이 가능한 상태가 아닙니다.");
        }
        Date currentDate = DateUtils.getSysDate();
        return Ticket.builder()
                .reservationId(this.reservationId)
                .concertId(this.concertId)
                .performanceId(this.performanceId)
                .areaId(this.areaId)
                .concertName(this.concertName)
                .artistName(this.artistName)
                .performedAt(this.performedAt)
                .areaName(this.areaName)
                .seatNumber(this.seatNumber)
                .isReceiveOnline(this.isReceiveOnline)
                .isReceiveOnSite(this.isReceiveOnSite)
                .isReceiveByPost(this.isReceiveByPost)
                .ticketPrice(this.ticketPrice)
                .reservedAt(currentDate)
                .publishedAt(reservation.isReceivedOnline() ? currentDate : this.publishedAt)
                .receivedAt(reservation.isReceivedOnline() ? currentDate : this.receivedAt)
                .build();
    }
}
