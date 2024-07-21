package com.io.hhplus.concert.infrastructure.reservation.repository.jpaRepository;

import com.io.hhplus.concert.common.enums.ReservationStatus;
import com.io.hhplus.concert.domain.reservation.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {
    Optional<ReservationEntity> findByIdAndCustomerId(Long id, Long customerId);
    List<ReservationEntity> findAllByReservationStatus(ReservationStatus reservationStatus);
}
