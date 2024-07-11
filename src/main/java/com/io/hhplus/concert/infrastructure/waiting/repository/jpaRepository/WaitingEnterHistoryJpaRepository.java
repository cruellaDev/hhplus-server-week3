package com.io.hhplus.concert.infrastructure.waiting.repository.jpaRepository;

import com.io.hhplus.concert.infrastructure.waiting.entity.WaitingEnterHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WaitingEnterHistoryJpaRepository extends JpaRepository<WaitingEnterHistory, Long> {
    @Query("SELECT T.id FROM WaitingEnterHistory T WHERE T.deleatedAt IS NULL ORDER BY T.id DESC LIMIT 1")
    Optional<Long> findOneIdOrderByIdDesc();

    @Query("SELECT T.id FROM WaitingEnterHistory T WHERE T.waitingId = :waitingId AND T.deleatedAt IS NULL ORDER BY T.id DESC LIMIT 1")
    Optional<Long> findOneIdByWaitingIdOrderByIdDesc(@Param("waitingId") Long waitingId);
}
