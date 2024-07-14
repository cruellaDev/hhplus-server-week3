package com.io.hhplus.concert.infrastructure.waiting.repository.impl;

import com.io.hhplus.concert.common.enums.WaitingStatus;
import com.io.hhplus.concert.domain.waiting.model.WaitingEnterHistory;
import com.io.hhplus.concert.domain.waiting.model.WaitingQueue;
import com.io.hhplus.concert.domain.waiting.repository.WaitingRepository;
import com.io.hhplus.concert.infrastructure.waiting.repository.jpaRepository.WaitingEnterHistoryJpaRepository;
import com.io.hhplus.concert.infrastructure.waiting.repository.jpaRepository.WaitingQueueJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class WaitingRepositoryImpl implements WaitingRepository {

    private final WaitingQueueJpaRepository waitingQueueJpaRepository;
    private final WaitingEnterHistoryJpaRepository waitingEnterHistoryJpaRepository;

    private WaitingQueue mapEntityToDto(com.io.hhplus.concert.infrastructure.waiting.entity.WaitingQueue entity) {
        return WaitingQueue.create(entity.getId(), entity.getCustomerId(), entity.getToken(), entity.getWaitingStatus(), entity.getAuditSection().getCreatedAt(), entity.getDeleatedAt());
    }

    private WaitingEnterHistory mapEntityToDto(com.io.hhplus.concert.infrastructure.waiting.entity.WaitingEnterHistory entity) {
        return WaitingEnterHistory.create(entity.getId(), entity.getWaitingId(), entity.getAuditSection().getCreatedAt());
    }

    private com.io.hhplus.concert.infrastructure.waiting.entity.WaitingQueue mapDtoToEntity(WaitingQueue dto) {
        return com.io.hhplus.concert.infrastructure.waiting.entity.WaitingQueue.builder()
                .id(dto.waitingId())
                .customerId(dto.customerId())
                .token(dto.token())
                .waitingStatus(dto.waitingStatus())
                .build();
    }

    private com.io.hhplus.concert.infrastructure.waiting.entity.WaitingEnterHistory mapDtoToEntity(WaitingEnterHistory dto) {
        return com.io.hhplus.concert.infrastructure.waiting.entity.WaitingEnterHistory.builder()
                .id(dto.waitingEnterHistoryId())
                .waitingId(dto.waitingId())
                .build();
    }

    @Override
    public Optional<WaitingQueue> findWaitingQueueByCustomerIdAndWaitingStatus(Long customerId, WaitingStatus waitingStatus) {
        return waitingQueueJpaRepository.findByCustomerIdAndWaitingStatus(customerId, waitingStatus)
                .map(this::mapEntityToDto);
    }

    @Override
    public WaitingQueue saveWaitingQueue(WaitingQueue waitingQueue) {
        return mapEntityToDto(waitingQueueJpaRepository.save(mapDtoToEntity(waitingQueue)));
    }

    @Override
    public WaitingEnterHistory saveWaitingEnterHistory(WaitingEnterHistory waitingEnterHistory) {
        return mapEntityToDto(waitingEnterHistoryJpaRepository.save(mapDtoToEntity(waitingEnterHistory)));
    }

    @Override
    public long countWaitingQueueByWaitingStatus(WaitingStatus waitingStatus) {
        return waitingQueueJpaRepository.findAllByWaitingStatus(waitingStatus)
                .stream()
                .filter(entity -> TimeUnit.MILLISECONDS.toSeconds((new Date()).getTime() - entity.getAuditSection().getCreatedAt().getTime()) <=300)
                .count();
    }

    @Override
    public Optional<Long> findOneWaitingEnterHistoryIdOrderByWaitingEnterHistoryIdAsc() {
        return waitingEnterHistoryJpaRepository.findOneIdOrderByIdAsc();
    }

    @Override
    public Optional<Long> findOneWaitingEnterHistoryIdByWaitingIdOrderByWaitingEnterHistoryIdDesc(Long waitingId) {
        return waitingEnterHistoryJpaRepository.findOneIdByWaitingIdOrderByIdDesc(waitingId);
    }

    @Override
    public List<WaitingQueue> findAllExpiredWaitingQueue() {
        return waitingQueueJpaRepository.findAllByDeleatedAtIsNull()
                .stream()
                .filter(entity -> (entity.getWaitingStatus().equals(WaitingStatus.ACTIVE) // TODO 날짜 조건 추가 필요
                        || entity.getWaitingStatus().equals(WaitingStatus.EXPIRED))
                )
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllWaitingQueue() {
        waitingQueueJpaRepository.deleteAll();
    }

    @Override
    public void deleteAllWaitingEnterHistory() {
        waitingEnterHistoryJpaRepository.deleteAll();
    }
}
