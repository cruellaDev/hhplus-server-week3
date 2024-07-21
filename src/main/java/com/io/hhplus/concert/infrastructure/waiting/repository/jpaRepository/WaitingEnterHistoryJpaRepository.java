package com.io.hhplus.concert.infrastructure.waiting.repository.jpaRepository;

import com.io.hhplus.concert.domain.waiting.entity.WaitingEnterHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WaitingEnterHistoryJpaRepository extends JpaRepository<WaitingEnterHistoryEntity, Long> {
    @Query("SELECT T.id FROM WaitingEnterHistoryEntity T WHERE T.deleatedAt IS NULL ORDER BY T.id ASC LIMIT 1")
    Optional<Long> findOneIdOrderByIdAsc();

    @Query("SELECT T.id FROM WaitingEnterHistoryEntity T WHERE T.waitingId = :waitingId AND T.deleatedAt IS NULL ORDER BY T.id DESC LIMIT 1")
    Optional<Long> findOneIdByWaitingIdOrderByIdDesc(@Param("waitingId") Long waitingId);
}
