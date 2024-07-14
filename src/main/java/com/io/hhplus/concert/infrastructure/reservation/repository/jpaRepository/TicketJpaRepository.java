package com.io.hhplus.concert.infrastructure.reservation.repository.jpaRepository;

import com.io.hhplus.concert.infrastructure.reservation.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketJpaRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByReservationId(Long reservationId);
}
