package com.io.hhplus.concert.infrastructure.concert.repository.jpaRepository;

import com.io.hhplus.concert.infrastructure.concert.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerformanceJpaRepository extends JpaRepository<Performance, Long> {
    List<Performance> findAllByConcertId(Long concertId);
}
