package com.io.hhplus.concert.infrastructure.concert.repository.jpaRepository;

import com.io.hhplus.concert.infrastructure.concert.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertJpaRepository extends JpaRepository<Concert, Long> {
}
