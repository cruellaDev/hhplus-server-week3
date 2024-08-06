package com.io.hhplus.concert.infrastructure.concert.repository.jpa;

import com.io.hhplus.concert.infrastructure.concert.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketJpaRepository extends JpaRepository<TicketEntity, Long> {
    @Query("SELECT T FROM TicketEntity T WHERE T.concertId =:concertId AND T.concertScheduleId =:concertScheduleId AND T.seatNumber =:seatNumber AND T.deletedAt IS NULL")
    List<TicketEntity> findOccupiedSeatsFromTicket(@Param("concertId") Long concertId,
                                                      @Param("concertScheduleId") Long concertScheduleId,
                                                      @Param("seatNumber") String seatNumber);
    List<TicketEntity> findAllByReservationId(Long reservationId);
}
