package com.io.hhplus.concert.domain.concert.repository;

import com.io.hhplus.concert.domain.concert.model.Performance;

import java.util.List;
import java.util.Optional;

public interface PerformanceRepository {
    List<Performance> findAvailableAllByConcertId(Long concertId);
    Optional<Performance> findAvailableOneByConcertIdAndPerformanceId(Long concertId, Long performanceId);
}
