package com.io.hhplus.concert.infrastructure.queue.repository.jpa;

import com.io.hhplus.concert.common.enums.QueueStatus;
import com.io.hhplus.concert.infrastructure.queue.entity.QueueTokenEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QueueTokenJpaRepository extends JpaRepository<QueueTokenEntity, Long> {
    Optional<QueueTokenEntity> findByCustomerIdAndDeletedAtIsNullOrderByIdDesc(Long customerId);
    Optional<QueueTokenEntity> findByQueueTokenAndDeletedAtIsNull(UUID queueToken);
    Long countByQueueStatusAndDeletedAtIsNull(QueueStatus queueStatus);
    Optional<QueueTokenEntity> findFirstByQueueStatusOrderByIdAsc(QueueStatus queueStatus);
    Optional<QueueTokenEntity> findFirstByQueueStatusOrderByIdDesc(QueueStatus queueStatus);

    @Query("SELECT Q FROM QueueTokenEntity Q WHERE Q.queueStatus = 'WAITING' AND Q.deletedAt IS NULL ORDER BY Q.id ASC")
    List<QueueTokenEntity> findWaitingStatusQueueTokenWithPagination(Pageable pageable);

    @Query("SELECT Q FROM QueueTokenEntity Q WHERE Q.queueStatus = 'ACTIVE' AND Q.auditSection.modifiedAt <:expirationDate AND Q.deletedAt IS NULL ORDER BY Q.id ASC")
    List<QueueTokenEntity> findActiveQueueTokenWithPagination(Pageable pageable);

    @Query("SELECT COUNT(Q) FROM QueueTokenEntity Q WHERE Q.queueStatus = 'ACTIVE' AND Q.auditSection.modifiedAt <:expirationDate AND Q.deletedAt IS NULL")
    Long countUpcomingExpiredQueueToken(@Param("expirationDate") Date expirationDate);
}
