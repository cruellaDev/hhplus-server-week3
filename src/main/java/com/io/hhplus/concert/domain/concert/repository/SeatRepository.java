package com.io.hhplus.concert.domain.concert.repository;

import com.io.hhplus.concert.domain.concert.model.Seat;

import java.util.List;

public interface SeatRepository {

    List<Seat> findSeats(Long concertId, Long performanceId);

//    Optional<Seat> findSeat(Long seatId);
//    List<Seat> findSeats(Long concertId, Long performanceId);
//    Seat save(Seat seat);
//    List<SeatModel> findAvailableAllByPerformanceId(Long performanceId);
//    Optional<SeatModel> findAvailableOneByPerformanceIdAndSeatId(Long performanceId, Long seatId);
//    Optional<SeatModel> findById(Long seatId);
//    SeatModel save(SeatModel seatModel);
//    List<SeatModel> findAllByReservationIdAndSeatStatus(Long reservationId, SeatStatus seatStatus);
}
