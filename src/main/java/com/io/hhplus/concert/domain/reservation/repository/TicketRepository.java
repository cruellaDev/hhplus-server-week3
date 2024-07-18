package com.io.hhplus.concert.domain.reservation.repository;

import com.io.hhplus.concert.common.enums.TicketStatus;
import com.io.hhplus.concert.domain.reservation.service.model.TicketModel;

import java.math.BigDecimal;
import java.util.List;

public interface TicketRepository {
    TicketModel save(TicketModel ticketModel);
    BigDecimal sumTicketPriceByReservationIdAndTicketStatus(Long reservationId, TicketStatus ticketStatus);
    List<TicketModel> findAllByReservationIdAndTicketStatus(Long reservationId, TicketStatus ticketStatus);
    List<TicketModel> saveAll(List<TicketModel> ticketModels);
}
