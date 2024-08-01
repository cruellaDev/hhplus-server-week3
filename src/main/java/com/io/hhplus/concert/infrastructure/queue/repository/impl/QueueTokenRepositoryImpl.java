package com.io.hhplus.concert.infrastructure.queue.repository.impl;

import com.io.hhplus.concert.common.enums.QueueStatus;
import com.io.hhplus.concert.domain.queue.model.QueueToken;
import com.io.hhplus.concert.domain.queue.QueueTokenRepository;
import com.io.hhplus.concert.infrastructure.queue.entity.QueueTokenEntity;
import com.io.hhplus.concert.infrastructure.queue.repository.jpa.QueueTokenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class QueueTokenRepositoryImpl implements QueueTokenRepository {

    private final QueueTokenJpaRepository queueTokenJpaRepository;

    @Override
    public Optional<QueueToken> findLatestQueueToken(Long customerId) {
        return Optional.empty();
    }

    @Override
    public Optional<QueueToken> findQueueToken(UUID queueToken) {
        return queueTokenJpaRepository.findByQueueTokenAndDeletedAtIsNull(queueToken)
                .map(QueueTokenEntity::toDomain);
    }

    @Override
    public Long countActiveQueueToken() {
        return queueTokenJpaRepository.countByQueueStatusAndDeletedAtIsNull(QueueStatus.ACTIVE);
    }

    @Override
    public QueueToken saveQueueToken(QueueToken queueToken) {
        return queueTokenJpaRepository.save(QueueTokenEntity.from(queueToken)).toDomain();
    }

    @Override
    public Optional<QueueToken> findFirstWaitingQueueToken() {
        return queueTokenJpaRepository.findFirstByQueueStatusOrderByIdAsc(QueueStatus.WAITING)
                .map(QueueTokenEntity::toDomain);
    }

    @Override
    public Optional<QueueToken> findLastWaitingQueueToken() {
        return queueTokenJpaRepository.findFirstByQueueStatusOrderByIdDesc(QueueStatus.WAITING)
                .map(QueueTokenEntity::toDomain);
    }

    @Override
    public List<QueueToken> findUpcomingActiveQueueTokens(Pageable pageable) {
        return queueTokenJpaRepository.findWaitingStatusQueueTokenWithPagination(pageable)
                .stream()
                .map(QueueTokenEntity::toDomain)
                .collect(Collectors.toList())
                ;
    }

    @Override
    public List<QueueToken> findUpcomingExpiredQueueTokens(Pageable pageable) {
        return queueTokenJpaRepository.findActiveQueueTokenWithPagination(pageable)
                .stream()
                .map(QueueTokenEntity::toDomain)
                .collect(Collectors.toList())
        ;
    }

    @Override
    public List<QueueToken> saveAllQueueToken(List<QueueToken> queueTokens) {
        return queueTokenJpaRepository.saveAll(
                queueTokens.stream().map(QueueTokenEntity::from).collect(Collectors.toList())
        ).stream().map(QueueTokenEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public Long countUpcomingExpiredToken(Date expirationDate) {
        return queueTokenJpaRepository.countUpcomingExpiredQueueToken(expirationDate);
    }
}
