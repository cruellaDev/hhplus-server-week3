package com.io.hhplus.concert.infrastructure.concert.repository.jpa;

import com.io.hhplus.concert.infrastructure.concert.entity.ConcertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConcertJpaRepository extends JpaRepository<ConcertEntity, Long> {
    @Query("SELECT C FROM ConcertEntity C WHERE C.concertStatus = 'AVAILABLE' AND NOW() BETWEEN C.bookBeginAt AND C.bookEndAt AND C.deletedAt IS NULL")
    List<ConcertEntity> findAvailableConcerts();
}
