package com.io.hhplus.concert.infrastructure.concert.repository.jpa;

import com.io.hhplus.concert.infrastructure.concert.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TicketJpaRepository extends JpaRepository<TicketEntity, Long> {
    @Query("SELECT T FROM TicketEntity T WHERE T.concertId =:concertId AND T.performanceId =:performanceId AND T.areaId =:areaId AND T.seatNumber =: seatNumber AND T.deletedAt IS NULL")
    Optional<TicketEntity> findNotOccupiedSeatFromTicket(@Param("concertId") Long concertId,
                                                         @Param("performanceId") Long performanceId,
                                                         @Param("areaId") Long areaId,
                                                         @Param("seatNumber") String seatNumber);
    List<TicketEntity> findAllByReservationId(Long reservationId);
}
