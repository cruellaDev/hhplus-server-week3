package com.io.hhplus.concert.infrastructure.waiting.repository.jpaRepository;

import com.io.hhplus.concert.common.enums.WaitingStatus;
import com.io.hhplus.concert.domain.waiting.entity.WaitingQueueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WaitingQueueJpaRepository extends JpaRepository<WaitingQueueEntity, Long> {

    @Query("SELECT T FROM WaitingQueueEntity T WHERE T.customerId = :customerId AND T.waitingStatus = :waitingStatus AND T.deleatedAt IS NULL ORDER BY T.id DESC LIMIT 1")
    Optional<WaitingQueueEntity> findByCustomerIdAndWaitingStatus(@Param("customerId") Long customerId, @Param("waitingStatus") WaitingStatus waitingStatus);

    @Query("SELECT T FROM WaitingQueueEntity T WHERE T.waitingStatus = :waitingStatus AND T.deleatedAt IS NULL")
    List<WaitingQueueEntity> findAllByWaitingStatus(@Param("waitingStatus") WaitingStatus waitingStatus);

    @Query("SELECT T FROM WaitingQueueEntity T WHERE T.deleatedAt IS NULL")
    List<WaitingQueueEntity> findAllByDeleatedAtIsNull();
}
