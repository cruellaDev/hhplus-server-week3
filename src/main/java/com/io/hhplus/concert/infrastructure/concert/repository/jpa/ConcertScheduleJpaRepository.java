package com.io.hhplus.concert.infrastructure.concert.repository.jpa;

import com.io.hhplus.concert.infrastructure.concert.entity.ConcertScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertScheduleEntity, Long> {
    @Query("SELECT SC FROM ConcertScheduleEntity SC INNER JOIN ConcertEntity C ON C.id = SC.concertId WHERE SC.concertId =:concertId AND C.concertStatus = 'AVAILABLE' AND NOW() BETWEEN C.bookBeginAt AND C.bookEndAt AND NOW() BETWEEN C.bookBeginAt AND SC.performedAt AND C.deletedAt IS NULL AND SC.deletedAt IS NULL")
    List<ConcertScheduleEntity> findAllByDeletedAtIsNullAndConcertIdEquals(@Param("concertId") Long concertId);
    Optional<ConcertScheduleEntity> findByIdAndConcertIdAndDeletedAtIsNull(Long concertScheduleId, Long concertId);
}
