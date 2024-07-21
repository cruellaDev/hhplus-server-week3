package com.io.hhplus.concert.infrastructure.reservation.repository.jpaRepository;

import com.io.hhplus.concert.domain.reservation.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketJpaRepository extends JpaRepository<TicketEntity, Long> {
    List<TicketEntity> findAllByReservationId(Long reservationId);
}
