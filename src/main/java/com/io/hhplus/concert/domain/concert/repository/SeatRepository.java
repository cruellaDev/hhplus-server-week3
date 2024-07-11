package com.io.hhplus.concert.domain.concert.repository;

import com.io.hhplus.concert.common.enums.SeatStatus;
import com.io.hhplus.concert.domain.concert.model.Seat;

import java.util.List;
import java.util.Optional;

public interface SeatRepository {
    List<Seat> findAvailableAllByPerformanceId(Long performanceId);
    Optional<Seat> findAvailableOneByPerformanceIdAndSeatId(Long performanceId, Long seatId);
    Optional<Seat> findById(Long seatId);
    Seat save(Seat seat);
    List<Seat> findAllByReservationIdAndSeatStatus(Long reservationId, SeatStatus seatStatus);
}
