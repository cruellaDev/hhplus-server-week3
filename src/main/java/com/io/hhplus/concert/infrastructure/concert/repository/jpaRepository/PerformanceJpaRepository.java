package com.io.hhplus.concert.infrastructure.concert.repository.jpaRepository;

import com.io.hhplus.concert.domain.concert.entity.PerformanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerformanceJpaRepository extends JpaRepository<PerformanceEntity, Long> {
    List<PerformanceEntity> findAllByConcertId(Long concertId);
}
