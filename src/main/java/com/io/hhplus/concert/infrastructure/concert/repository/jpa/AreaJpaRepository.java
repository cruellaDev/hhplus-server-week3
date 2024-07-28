package com.io.hhplus.concert.infrastructure.concert.repository.jpa;

import com.io.hhplus.concert.infrastructure.concert.entity.AreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AreaJpaRepository extends JpaRepository<AreaEntity, Long> {
    List<AreaEntity> findAllByConcertIdAndPerformanceIdAndDeletedAtIsNull(Long concertId, Long performanceId);
}
