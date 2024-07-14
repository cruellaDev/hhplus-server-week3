package com.io.hhplus.concert.infrastructure.concert.repository.impl;

import com.io.hhplus.concert.domain.concert.model.Performance;
import com.io.hhplus.concert.domain.concert.repository.PerformanceRepository;
import com.io.hhplus.concert.infrastructure.concert.repository.jpaRepository.PerformanceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PerformanceRepositoryImpl implements PerformanceRepository {

    private final PerformanceJpaRepository performanceJpaRepository;

    private boolean isEqualConcertId(Long concertId, Long targetConcertId) {
        return concertId != null && targetConcertId != null && concertId.compareTo(targetConcertId) == 0;
    }
    private boolean isNotDeleted(Date deletedAt) {
        return deletedAt == null;
    }
    private boolean isBeforePerformed(Date performedAt) {
        return performedAt != null && (new Date()).before(performedAt);
    }

    private Performance mapEntityToResponseModel(com.io.hhplus.concert.infrastructure.concert.entity.Performance entity) {
        return Performance.create(entity.getId(), entity.getConcertPrice(), entity.getCapacityLimit(), entity.getPerformedAt());
    };

    @Override
    public List<Performance> findAvailableAllByConcertId(Long concertId) {
        return performanceJpaRepository.findAllByConcertId(concertId)
                .stream()
                .filter(entity -> isNotDeleted(entity.getDeletedAt())
                        && isBeforePerformed(entity.getPerformedAt()))
                .map(this::mapEntityToResponseModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Performance> findAvailableOneByConcertIdAndPerformanceId(Long concertId, Long performanceId) {
        return performanceJpaRepository.findById(performanceId)
                .filter(entity -> isEqualConcertId(entity.getConcertId(), concertId)
                        && isNotDeleted(entity.getDeletedAt())
                        && isBeforePerformed(entity.getPerformedAt()))
                .map(this::mapEntityToResponseModel);
    }
}
