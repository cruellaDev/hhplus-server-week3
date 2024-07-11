package com.io.hhplus.concert.infrastructure.reservation.repository.jpaRepository;

import com.io.hhplus.concert.common.enums.ReservationStatus;
import com.io.hhplus.concert.infrastructure.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByIdAndCustomerId(Long id, Long customerId);
    List<Reservation> findAllByReservationStatus(ReservationStatus reservationStatus);
}
