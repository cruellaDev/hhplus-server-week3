package com.io.hhplus.concert.domain.concert.model;

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
                .build();
    }
}
