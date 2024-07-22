package com.io.hhplus.concert.infrastructure.concert.repository.jpa;

import com.io.hhplus.concert.infrastructure.concert.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatJpaRepository extends JpaRepository<SeatEntity, Long> {
    List<SeatEntity> findAllByConcertIdAndPerformanceId(Long concertId, Long performanceId);



//    List<SeatEntity> findAllByPerformanceId(Long performanceId);
//
//    @Query("SELECT S FROM SeatEntity S INNER JOIN ReservationEntity R ON R.reservationId = :reservationId AND R.deletedAt IS NULL INNER JOIN TicketEntity T ON T.reservationId = R.reservationId AND T.seatId = S.seatId AND T.deletedAt IS NULL WHERE S.deletedAt IS NULL")
//    List<SeatEntity> findAllWaitingSeatsByReservationId(@Param("reservationId") Long reservationId);
}
