package com.io.hhplus.concert.domain.waiting.repository;

import com.io.hhplus.concert.common.enums.WaitingStatus;
import com.io.hhplus.concert.domain.waiting.model.WaitingEnterHistory;
import com.io.hhplus.concert.domain.waiting.model.WaitingQueue;

import java.util.List;
import java.util.Optional;

public interface WaitingRepository {
    // 고객 대기열 정보 조회
    Optional<WaitingQueue> findWaitingQueueByCustomerIdAndWaitingStatus(Long customerId, WaitingStatus waitingStatus);
    // 고객 대기열 저장
    WaitingQueue saveWaitingQueue(WaitingQueue waitingQueue);
    // 대기열 진입 이력 저장
    WaitingEnterHistory saveWaitingEnterHistory(WaitingEnterHistory waitingEnterHistory);
    // 대기 인원 조회
    long countWaitingQueueByWaitingStatus(WaitingStatus waitingStatus);
    // 대기진입이력 마지막 pk 번호 조회
    Optional<Long> findOneWaitingEnterHistoryIdOrderByWaitingEnterHistoryIdAsc();
    // 대기진입이력 대기열 별 마지막 pk 번호 조회
    Optional<Long> findOneWaitingEnterHistoryIdByWaitingIdOrderByWaitingEnterHistoryIdDesc(Long waitingId);
    // 만료된 토큰 모두 조회
    List<WaitingQueue> findAllExpiredWaitingQueue();
    // 대기열 모든 기록 삭제
    void deleteAllWaitingQueue();
    void deleteAllWaitingEnterHistory();
}
