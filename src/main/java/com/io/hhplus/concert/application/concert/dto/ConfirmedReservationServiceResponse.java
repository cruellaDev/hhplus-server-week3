package com.io.hhplus.concert.application.concert.dto;

import com.io.hhplus.concert.common.enums.ReceiveMethod;
import com.io.hhplus.concert.domain.concert.model.Reservation;
import com.io.hhplus.concert.domain.concert.model.Ticket;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record ConfirmedReservationServiceResponse(
        ReservationResponse reservation,
        List<TicketResponse> tickets
) {
    public static ConfirmedReservationServiceResponse of(Reservation reservation, List<Ticket> tickets) {
        return ConfirmedReservationServiceResponse.builder()
                .reservation(ReservationResponse.from(reservation))
                .tickets(tickets.stream()
                                .map(TicketResponse::from)
                                .collect(Collectors.toList()))
                .build();
    }

    @Builder
    public record ReservationResponse(
            Long reservationId,
            Long customerId,
            String reserverName,
            ReceiveMethod receiveMethod,
            String receiverName,
            String receivePostCode,
            String receiveBaseAddress,
            String receiveDetailAddress
    ) {
        public static ReservationResponse from(Reservation reservation) {
            return ReservationResponse.builder()
                    .reservationId(reservation.reservationId())
                    .customerId(reservation.customerId())
                    .reserverName(reservation.reserverName())
                    .receiveMethod(reservation.receiveMethod())
                    .receiverName(reservation.receiverName())
                    .receivePostCode(reservation.receivePostcode())
                    .receiveBaseAddress(reservation.receiveBaseAddress())
                    .receiveDetailAddress(reservation.receiveDetailAddress())
                    .build();
        }
    }

    @Builder
    public record TicketResponse(
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
            Date cancelAcceptedAt
    ) {
        public static TicketResponse from(Ticket ticket) {
            return TicketResponse.builder()
                    .ticketId(ticket.ticketId())
                    .reservationId(ticket.reservationId())
                    .concertId(ticket.concertId())
                    .performanceId(ticket.performanceId())
                    .areaId(ticket.areaId())
                    .concertName(ticket.concertName())
                    .artistName(ticket.artistName())
                    .performedAt(ticket.performedAt())
                    .areaName(ticket.areaName())
                    .seatNumber(ticket.seatNumber())
                    .isReceiveOnline(ticket.isReceiveOnline())
                    .isReceiveOnSite(ticket.isReceiveOnSite())
                    .isReceiveByPost(ticket.isReceiveByPost())
                    .ticketPrice(ticket.ticketPrice())
                    .reservedAt(ticket.reservedAt())
                    .publishedAt(ticket.publishedAt())
                    .receivedAt(ticket.receivedAt())
                    .cancelAcceptedAt(ticket.cancelAcceptedAt())
                    .build();
        }
    }
}
