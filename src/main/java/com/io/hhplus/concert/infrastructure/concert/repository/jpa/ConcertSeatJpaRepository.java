package com.io.hhplus.concert.infrastructure.concert.repository.jpa;

import com.io.hhplus.concert.infrastructure.concert.entity.ConcertSeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeatEntity, Long> {
    Optional<ConcertSeatEntity> findByConcertIdAndConcertScheduleIdAndDeletedAtIsNull(Long concertId, Long concertScheduleId);
}
