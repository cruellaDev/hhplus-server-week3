package com.io.hhplus.concert.infrastructure.concert.repository.jpa;

import com.io.hhplus.concert.infrastructure.concert.entity.PerformanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PerformanceJpaRepository extends JpaRepository<PerformanceEntity, Long> {
    List<PerformanceEntity> findAllByDeletedAtIsNullAndConcertIdEquals(Long concertId);
    Optional<PerformanceEntity> findByIdAndConcertId(Long performanceId, Long concertId);
}
