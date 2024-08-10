package com.io.hhplus.concert.infrastructure.concert.repository.jpa;

import com.io.hhplus.concert.infrastructure.concert.entity.ConcertSeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeatEntity, Long> {
    @Query("SELECT ST FROM ConcertSeatEntity ST INNER JOIN ConcertEntity C ON C.id = ST.concertId INNER JOIN ConcertScheduleEntity SH ON SH.concertId = C.id AND SH.id = ST.concertScheduleId WHERE ST.concertId =:concertId AND ST.concertScheduleId =:concertScheduleId AND C.concertStatus = 'AVAILABLE' AND NOW() BETWEEN C.bookBeginAt AND C.bookEndAt AND C.deletedAt IS NULL AND SH.deletedAt IS NULL AND ST.deletedAt IS NULL")
    Optional<ConcertSeatEntity> findByConcertIdAndConcertScheduleIdAndDeletedAtIsNull(@Param("concertId") Long concertId,
                                                                                      @Param("concertScheduleId") Long concertScheduleId);
}
