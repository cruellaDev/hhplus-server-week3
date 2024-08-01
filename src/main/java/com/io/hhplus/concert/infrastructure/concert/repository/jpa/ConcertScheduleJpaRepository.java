package com.io.hhplus.concert.infrastructure.concert.repository.jpa;

import com.io.hhplus.concert.infrastructure.concert.entity.ConcertScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertScheduleEntity, Long> {
    List<ConcertScheduleEntity> findAllByDeletedAtIsNullAndConcertIdEquals(Long concertId);
    Optional<ConcertScheduleEntity> findByIdAndConcertIdAndDeletedAtIsNull(Long concertScheduleId, Long concertId);
}
