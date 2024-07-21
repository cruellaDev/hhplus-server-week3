package com.io.hhplus.concert.infrastructure.concert.repository.jpaRepository;

import com.io.hhplus.concert.domain.concert.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatJpaRepository extends JpaRepository<SeatEntity, Long> {
    List<SeatEntity> findAllByPerformanceId(Long performanceId);

    @Query("SELECT S FROM SeatEntity S INNER JOIN ReservationEntity R ON R.id = :reservationId AND R.deletedAt IS NULL INNER JOIN TicketEntity T ON T.reservationId = R.id AND T.seatId = S.id AND T.deletedAt IS NULL WHERE S.deletedAt IS NULL")
    List<SeatEntity> findAllWaitingSeatsByReservationId(@Param("reservationId") Long reservationId);
}
