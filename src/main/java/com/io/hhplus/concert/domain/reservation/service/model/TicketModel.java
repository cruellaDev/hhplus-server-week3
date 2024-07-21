package com.io.hhplus.concert.domain.reservation.service.model;

import com.io.hhplus.concert.common.enums.TicketStatus;
import com.io.hhplus.concert.domain.concert.service.model.ConcertModel;
import com.io.hhplus.concert.domain.concert.service.model.PerformanceModel;
import com.io.hhplus.concert.domain.concert.service.model.SeatModel;

import java.math.BigDecimal;
import java.util.Date;

public record TicketModel(
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
    public static TicketModel create(Long ticketId, Long reservationId, Long concertId, Long performanceId, Long seatId, String concertName, String artistName, Boolean isReceiveOnline, Boolean isReceiveOnSite, Boolean isReceiveByPost, BigDecimal price, Date performedAt, String seatNo, TicketStatus ticketStatus, Date reservedAt, Date publishedAt, Date receivedAt, Date cancelAcceptedAt, Date cancelApprovedAt) {
        return new TicketModel(ticketId,
                reservationId, concertId, performanceId, seatId,
                concertName, artistName,
                isReceiveOnline, isReceiveOnSite, isReceiveByPost,
                price, performedAt, seatNo,
                ticketStatus,
                reservedAt, publishedAt, receivedAt,
                cancelAcceptedAt, cancelApprovedAt);
    }

    public static TicketModel makeTicketOf(ReservationModel reservationModel, ConcertModel concertModel, PerformanceModel performanceModel, SeatModel seatModel) {
        return create(
                null,
                reservationModel.reservationId(),
                concertModel.concertId(),
                performanceModel.performanceId(),
                seatModel.seatId(),
                concertModel.concertName(),
                concertModel.artistName(),
                true, // 아직 기능 구현이 없어서 true로 보냄
                true,
                true,
                performanceModel.price(),
                performanceModel.performedAt(),
                seatModel.seatNo(),
                TicketStatus.PAY_WAITING,
                null,
                null,
                null,
                null,
                null
                );
    }

    public static TicketModel changeByTicketStatus(TicketModel ticketModel, TicketStatus ticketStatus) {
        return create(
                ticketModel.ticketId,
                ticketModel.reservationId(),
                ticketModel.concertId(),
                ticketModel.performanceId(),
                ticketModel.seatId(),
                ticketModel.concertName(),
                ticketModel.artistName(),
                true, // 아직 기능 구현이 없어서 true로 보냄
                true,
                true,
                ticketModel.price(),
                ticketModel.performedAt(),
                ticketModel.seatNo(),
                ticketStatus,
                ticketStatus.equals(TicketStatus.PAYED) ? new Date() : ticketModel.reservedAt,
                ticketStatus.equals(TicketStatus.PUBLISHED) ? new Date() : ticketModel.publishedAt,
                ticketStatus.equals(TicketStatus.RECEIVED) ? new Date() : ticketModel.receivedAt,
                ticketStatus.equals(TicketStatus.CANCELLED_AFTER_PAY_WAITING)
                        || ticketStatus.equals(TicketStatus.CANCELLED_AFTER_PAYED)
                        || ticketStatus.equals(TicketStatus.CANCELLED_AFTER_PUBLISHED)
                        || ticketStatus.equals(TicketStatus.CANCELLED_AFTER_RECEIVED) ? new Date() : ticketModel.cancelAcceptedAt,
                ticketStatus.equals(TicketStatus.CANCELLED_AFTER_PAY_WAITING)
                        || ticketStatus.equals(TicketStatus.CANCELLED_AFTER_PAYED)
                        || ticketStatus.equals(TicketStatus.CANCELLED_AFTER_PUBLISHED) ? new Date() : ticketModel.cancelApprovedAt
        );
    }
}
