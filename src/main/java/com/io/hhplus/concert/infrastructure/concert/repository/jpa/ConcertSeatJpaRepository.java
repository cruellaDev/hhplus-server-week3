package com.io.hhplus.concert.infrastructure.concert.repository.jpa;

import com.io.hhplus.concert.infrastructure.concert.entity.ConcertSeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeatEntity, Long> {
    @Query("SELECT ST FROM ConcertSeatEntity ST WHERE ST.concertId =:concertId AND ST.concertScheduleId =:concertScheduleId AND ST.deletedAt IS NULL")
    Optional<ConcertSeatEntity> findByConcertIdAndConcertScheduleIdAndDeletedAtIsNull(@Param("concertId") Long concertId,
                                                                                      @Param("concertScheduleId") Long concertScheduleId);
}
