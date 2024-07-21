package com.io.hhplus.concert.domain.concert.repository;

import com.io.hhplus.concert.domain.concert.service.model.PerformanceModel;

import java.util.List;
import java.util.Optional;

public interface PerformanceRepository {
    List<PerformanceModel> findAvailableAllByConcertId(Long concertId);
    Optional<PerformanceModel> findAvailableOneByConcertIdAndPerformanceId(Long concertId, Long performanceId);
}
