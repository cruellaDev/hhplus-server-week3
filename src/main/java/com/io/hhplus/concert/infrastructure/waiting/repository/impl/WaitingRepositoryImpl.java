package com.io.hhplus.concert.infrastructure.waiting.repository.impl;

import com.io.hhplus.concert.common.enums.WaitingStatus;
import com.io.hhplus.concert.common.utils.DateUtils;
import com.io.hhplus.concert.domain.waiting.entity.WaitingEnterHistoryEntity;
import com.io.hhplus.concert.domain.waiting.entity.WaitingQueueEntity;
import com.io.hhplus.concert.domain.waiting.service.model.WaitingEnterHistoryModel;
import com.io.hhplus.concert.domain.waiting.service.model.WaitingQueueModel;
import com.io.hhplus.concert.domain.waiting.repository.WaitingRepository;
import com.io.hhplus.concert.infrastructure.waiting.repository.jpaRepository.WaitingEnterHistoryJpaRepository;
import com.io.hhplus.concert.infrastructure.waiting.repository.jpaRepository.WaitingQueueJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class WaitingRepositoryImpl implements WaitingRepository {

    private final WaitingQueueJpaRepository waitingQueueJpaRepository;
    private final WaitingEnterHistoryJpaRepository waitingEnterHistoryJpaRepository;

    private WaitingQueueModel mapEntityToDto(WaitingQueueEntity entity) {
        return WaitingQueueModel.create(entity.getId(), entity.getCustomerId(), entity.getToken(), entity.getWaitingStatus(), entity.getAuditSection().getCreatedAt(), entity.getDeleatedAt());
    }

    private WaitingEnterHistoryModel mapEntityToDto(WaitingEnterHistoryEntity entity) {
        return WaitingEnterHistoryModel.create(entity.getId(), entity.getWaitingId(), entity.getAuditSection().getCreatedAt());
    }

    private WaitingQueueEntity mapDtoToEntity(WaitingQueueModel dto) {
        return WaitingQueueEntity.builder()
                .id(dto.waitingId())
                .customerId(dto.customerId())
                .token(dto.token())
                .waitingStatus(dto.waitingStatus())
                .build();
    }

    private WaitingEnterHistoryEntity mapDtoToEntity(WaitingEnterHistoryModel dto) {
        return WaitingEnterHistoryEntity.builder()
                .id(dto.waitingEnterHistoryId())
                .waitingId(dto.waitingId())
                .build();
    }

    @Override
    public Optional<WaitingQueueModel> findWaitingQueueByCustomerIdAndWaitingStatus(Long customerId, WaitingStatus waitingStatus) {
        return waitingQueueJpaRepository.findByCustomerIdAndWaitingStatus(customerId, waitingStatus)
                .map(this::mapEntityToDto);
    }

    @Override
    public WaitingQueueModel saveWaitingQueue(WaitingQueueModel waitingQueueModel) {
        return mapEntityToDto(waitingQueueJpaRepository.save(mapDtoToEntity(waitingQueueModel)));
    }

    @Override
    public WaitingEnterHistoryModel saveWaitingEnterHistory(WaitingEnterHistoryModel waitingEnterHistoryModel) {
        return mapEntityToDto(waitingEnterHistoryJpaRepository.save(mapDtoToEntity(waitingEnterHistoryModel)));
    }

    @Override
    public long countWaitingQueueByWaitingStatus(WaitingStatus waitingStatus) {
        return waitingQueueJpaRepository.findAllByWaitingStatus(waitingStatus)
                .stream()
                .filter(entity -> DateUtils.calculateDuration(DateUtils.getSysDate(), entity.getAuditSection().getCreatedAt()) <=300)
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
    public List<WaitingQueueModel> findAllExpiredWaitingQueue() {
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
