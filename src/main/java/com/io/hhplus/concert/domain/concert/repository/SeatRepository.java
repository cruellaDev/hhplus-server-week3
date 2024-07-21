package com.io.hhplus.concert.domain.concert.repository;

import com.io.hhplus.concert.common.enums.SeatStatus;
import com.io.hhplus.concert.domain.concert.service.model.SeatModel;

import java.util.List;
import java.util.Optional;

public interface SeatRepository {
    List<SeatModel> findAvailableAllByPerformanceId(Long performanceId);
    Optional<SeatModel> findAvailableOneByPerformanceIdAndSeatId(Long performanceId, Long seatId);
    Optional<SeatModel> findById(Long seatId);
    SeatModel save(SeatModel seatModel);
    List<SeatModel> findAllByReservationIdAndSeatStatus(Long reservationId, SeatStatus seatStatus);
}
