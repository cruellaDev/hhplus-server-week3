package com.io.hhplus.concert.infrastructure.concert.repository.impl;

import com.io.hhplus.concert.infrastructure.concert.entity.PerformanceEntity;
import com.io.hhplus.concert.domain.concert.model.Performance;
import com.io.hhplus.concert.domain.concert.repository.PerformanceRepository;
import com.io.hhplus.concert.infrastructure.concert.repository.jpa.PerformanceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PerformanceRepositoryImpl implements PerformanceRepository {

    private final PerformanceJpaRepository performanceJpaRepository;

    @Override
    public List<Performance> findPerformances(Long concertId) {
        return performanceJpaRepository.findAllByDeletedAtIsNullAndConcertIdEquals(concertId)
                .stream()
                .map(PerformanceEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Performance> findPerformance(Long performanceId, Long concertId) {
        return performanceJpaRepository.findByIdAndConcertId(performanceId, concertId)
                .map(PerformanceEntity::toDomain);
    }

}
