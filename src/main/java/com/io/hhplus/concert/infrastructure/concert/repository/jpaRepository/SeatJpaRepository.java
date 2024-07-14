package com.io.hhplus.concert.infrastructure.concert.repository.jpaRepository;

import com.io.hhplus.concert.infrastructure.concert.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatJpaRepository extends JpaRepository<Seat, Long> {
    List<Seat> findAllByPerformanceId(Long performanceId);

    @Query("SELECT S FROM Seat S INNER JOIN Reservation R ON R.id = :reservationId AND R.deletedAt IS NULL INNER JOIN Ticket T ON T.reservationId = R.id AND T.seatId = S.id AND T.deletedAt IS NULL WHERE S.deletedAt IS NULL")
    List<Seat> findAllWaitingSeatsByReservationId(@Param("reservationId") Long reservationId);
}
