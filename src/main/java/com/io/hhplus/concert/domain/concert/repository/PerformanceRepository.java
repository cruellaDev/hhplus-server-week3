package com.io.hhplus.concert.domain.concert.repository;

import com.io.hhplus.concert.domain.concert.model.Performance;

import java.util.List;
import java.util.Optional;

public interface PerformanceRepository {
    Optional<Performance> findPerformance(Long performanceId, Long concertId);
    List<Performance> findPerformances(Long concertId);
}
