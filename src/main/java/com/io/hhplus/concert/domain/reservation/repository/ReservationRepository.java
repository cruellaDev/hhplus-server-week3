package com.io.hhplus.concert.domain.reservation.repository;

import com.io.hhplus.concert.common.enums.ReservationStatus;
import com.io.hhplus.concert.domain.reservation.model.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
    Optional<Reservation> findByIdAndCustomerId(Long reservationId, Long customerId);
    List<Reservation> findAllByReservationStatus(ReservationStatus reservationStatus);
}
