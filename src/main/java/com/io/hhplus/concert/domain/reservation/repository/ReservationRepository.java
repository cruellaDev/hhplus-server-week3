package com.io.hhplus.concert.domain.reservation.repository;

import com.io.hhplus.concert.common.enums.ReservationStatus;
import com.io.hhplus.concert.domain.reservation.service.model.ReservationModel;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    ReservationModel save(ReservationModel reservationModel);
    Optional<ReservationModel> findByIdAndCustomerId(Long reservationId, Long customerId);
    List<ReservationModel> findAllByReservationStatus(ReservationStatus reservationStatus);
}
