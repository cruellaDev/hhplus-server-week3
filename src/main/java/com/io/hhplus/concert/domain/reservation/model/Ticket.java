package com.io.hhplus.concert.domain.reservation.model;

import com.io.hhplus.concert.common.enums.TicketStatus;
import com.io.hhplus.concert.domain.concert.model.Concert;
import com.io.hhplus.concert.domain.concert.model.Performance;
import com.io.hhplus.concert.domain.concert.model.Seat;

import java.math.BigDecimal;
import java.util.Date;

public record Ticket(
        Long ticketId,
        Long reservationId,
        Long concertId,
        Long performanceId,
        Long seatId,
        String concertName,
        String artistName,
        Boolean isReceiveOnline,
        Boolean isReceiveOnSite,
        Boolean isReceiveByPost,
        BigDecimal price,
        Date performedAt,
        String seatNo,
        TicketStatus ticketStatus,
        Date reservedAt,
        Date publishedAt,
        Date receivedAt,
        Date cancelAcceptedAt,
        Date cancelApprovedAt
) {
    public static Ticket create(Long ticketId, Long reservationId, Long concertId, Long performanceId, Long seatId, String concertName, String artistName, Boolean isReceiveOnline, Boolean isReceiveOnSite, Boolean isReceiveByPost, BigDecimal price, Date performedAt, String seatNo, TicketStatus ticketStatus, Date reservedAt, Date publishedAt, Date receivedAt, Date cancelAcceptedAt, Date cancelApprovedAt) {
        return new Ticket(ticketId,
                reservationId, concertId, performanceId, seatId,
                concertName, artistName,
                isReceiveOnline, isReceiveOnSite, isReceiveByPost,
                price, performedAt, seatNo,
                ticketStatus,
                reservedAt, publishedAt, receivedAt,
                cancelAcceptedAt, cancelApprovedAt);
    }

    public static Ticket makeTicketOf(Reservation reservation, Concert concert, Performance performance, Seat seat) {
        return create(
                null,
                reservation.reservationId(),
                concert.concertId(),
                performance.performanceId(),
                seat.seatId(),
                concert.concertName(),
                concert.artistName(),
                true, // 아직 기능 구현이 없어서 true로 보냄
                true,
                true,
                performance.price(),
                performance.performedAt(),
                seat.seatNo(),
                TicketStatus.PAY_WAITING,
                null,
                null,
                null,
                null,
                null
                );
    }

    public static Ticket changeByTicketStatus(Ticket ticket, TicketStatus ticketStatus) {
        return create(
                ticket.ticketId,
                ticket.reservationId(),
                ticket.concertId(),
                ticket.performanceId(),
                ticket.seatId(),
                ticket.concertName(),
                ticket.artistName(),
                true, // 아직 기능 구현이 없어서 true로 보냄
                true,
                true,
                ticket.price(),
                ticket.performedAt(),
                ticket.seatNo(),
                ticketStatus,
                ticketStatus.equals(TicketStatus.PAYED) ? new Date() : ticket.reservedAt,
                ticketStatus.equals(TicketStatus.PUBLISHED) ? new Date() : ticket.publishedAt,
                ticketStatus.equals(TicketStatus.RECEIVED) ? new Date() : ticket.receivedAt,
                ticketStatus.equals(TicketStatus.CANCELLED_AFTER_PAY_WAITING)
                        || ticketStatus.equals(TicketStatus.CANCELLED_AFTER_PAYED)
                        || ticketStatus.equals(TicketStatus.CANCELLED_AFTER_PUBLISHED)
                        || ticketStatus.equals(TicketStatus.CANCELLED_AFTER_RECEIVED) ? new Date() : ticket.cancelAcceptedAt,
                ticketStatus.equals(TicketStatus.CANCELLED_AFTER_PAY_WAITING)
                        || ticketStatus.equals(TicketStatus.CANCELLED_AFTER_PAYED)
                        || ticketStatus.equals(TicketStatus.CANCELLED_AFTER_PUBLISHED) ? new Date() : ticket.cancelApprovedAt
        );
    }
}
