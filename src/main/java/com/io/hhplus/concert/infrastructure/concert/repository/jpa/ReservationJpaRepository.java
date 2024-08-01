package com.io.hhplus.concert.infrastructure.concert.repository.jpa;

import com.io.hhplus.concert.domain.concert.model.Reservation;
import com.io.hhplus.concert.infrastructure.concert.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {
    @Query("SELECT R" +
            " FROM ReservationEntity R " +
            "WHERE R.customerId =:customerId " +
            "  AND R.deletedAt IS NULL " +
            "  AND EXISTS(SELECT T" +
            "                 FROM TicketEntity T" +
            "                WHERE T.concertId =: concertId" +
            "                  AND T.concertScheduleId =: concertScheduleId" +
            "                  AND T.seatNumber IN (: seatNumbers)" +
            "                  AND T.cancelApprovedAt IS NULL" +
            "                  AND T.deletedAt IS NULL)")
    Optional<Reservation> findReservationAlreadyExists(@Param("customerId") Long customerId,
                                                       @Param("concertId") Long concertId,
                                                       @Param("concertScheduleId") Long concertScheduleId,
                                                       @Param("seatNumbers") List<String> seatNumbers);
}
