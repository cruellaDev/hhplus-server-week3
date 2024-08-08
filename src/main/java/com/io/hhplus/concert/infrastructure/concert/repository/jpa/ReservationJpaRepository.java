package com.io.hhplus.concert.infrastructure.concert.repository.jpa;

import com.io.hhplus.concert.infrastructure.concert.entity.ReservationEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {
    @Query("SELECT R FROM ReservationEntity R WHERE R.customerId =:customerId AND R.deletedAt IS NULL AND (SELECT COUNT(T.id) FROM TicketEntity T WHERE T.reservationId = R.id AND T.concertId =:concertId AND T.concertScheduleId =:concertScheduleId AND T.seatNumber IN :seatNumbers AND T.cancelApprovedAt IS NULL AND T.deletedAt IS NULL) > 0")
    Optional<ReservationEntity> findCustomerReservationAlreadyExists(@Param("customerId") Long customerId,
                                                       @Param("concertId") Long concertId,
                                                       @Param("concertScheduleId") Long concertScheduleId,
                                                       @Param("seatNumbers") List<String> seatNumbers);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT R FROM ReservationEntity R WHERE R.customerId =:customerId AND R.deletedAt IS NULL AND (SELECT COUNT(T.id) FROM TicketEntity T WHERE T.reservationId = R.id AND T.concertId =:concertId AND T.concertScheduleId =:concertScheduleId AND T.seatNumber IN :seatNumbers AND T.cancelApprovedAt IS NULL AND T.deletedAt IS NULL) > 0")
    Optional<ReservationEntity> findCustomerReservationAlreadyExistsWithPessimisticLock(@Param("customerId") Long customerId,
                                                             @Param("concertId") Long concertId,
                                                             @Param("concertScheduleId") Long concertScheduleId,
                                                             @Param("seatNumbers") List<String> seatNumbers);

    @Query("SELECT R FROM ReservationEntity R WHERE R.deletedAt IS NULL AND (SELECT COUNT(T.id) FROM TicketEntity T WHERE T.concertId =:concertId AND T.concertScheduleId =:concertScheduleId AND T.seatNumber IN :seatNumbers AND T.cancelApprovedAt IS NULL AND T.deletedAt IS NULL) > 0")
    List<ReservationEntity> findReservationsAlreadyExists(@Param("concertId") Long concertId,
                                                          @Param("concertScheduleId") Long concertScheduleId,
                                                          @Param("seatNumbers") List<String> seatNumbers);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT R FROM ReservationEntity R WHERE R.deletedAt IS NULL AND (SELECT COUNT(T.id) FROM TicketEntity T WHERE T.concertId =:concertId AND T.concertScheduleId =:concertScheduleId AND T.seatNumber IN :seatNumbers AND T.cancelApprovedAt IS NULL AND T.deletedAt IS NULL) > 0")
    List<ReservationEntity> findReservationsAlreadyExistsWithPessimisticLock(@Param("concertId") Long concertId,
                                                          @Param("concertScheduleId") Long concertScheduleId,
                                                          @Param("seatNumbers") List<String> seatNumbers);
}
