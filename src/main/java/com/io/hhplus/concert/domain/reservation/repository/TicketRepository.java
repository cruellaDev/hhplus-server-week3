package com.io.hhplus.concert.domain.reservation.repository;

import com.io.hhplus.concert.common.enums.TicketStatus;
import com.io.hhplus.concert.domain.reservation.model.Ticket;

import java.math.BigDecimal;
import java.util.List;

public interface TicketRepository {
    Ticket save(Ticket ticket);
    BigDecimal sumTicketPriceByReservationIdAndTicketStatus(Long reservationId, TicketStatus ticketStatus);
    List<Ticket> findAllByReservationIdAndTicketStatus(Long reservationId, TicketStatus ticketStatus);
    List<Ticket> saveAll(List<Ticket> tickets);
}
