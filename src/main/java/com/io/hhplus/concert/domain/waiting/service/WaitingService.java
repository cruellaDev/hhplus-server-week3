package com.io.hhplus.concert.domain.waiting.service;

import com.io.hhplus.concert.common.enums.WaitingStatus;
import com.io.hhplus.concert.domain.waiting.model.WaitingEnterHistory;
import com.io.hhplus.concert.domain.waiting.model.WaitingQueue;
import com.io.hhplus.concert.domain.waiting.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class WaitingService {

    private final WaitingRepository waitingRepository;

    /**
     * 고객의 활성화된 대기열 토큰 조회
     * @param customerId 고객_ID
     * @return 대기열 토큰 정보
     */
    public WaitingQueue getActiveWaitingTokenByCustomerId(Long customerId) {
        return waitingRepository.findWaitingQueueByCustomerIdAndWaitingStatus(customerId, WaitingStatus.ACTIVE).orElseGet(WaitingQueue::noContents);
    }

    /**
     * 대기열 정보 검증
     * @param waitingQueue 대기열 정보
     * @return 유효 여부
     */
    public boolean meetsIfActiveWaitingQueueExists(WaitingQueue waitingQueue) {
        return waitingQueue != null
                && WaitingQueue.isAvailableWaitingId(waitingQueue.waitingId())
                && WaitingQueue.isAvailableCustomerId(waitingQueue.customerId())
                && WaitingQueue.isAvailableToken(waitingQueue.token())
                && WaitingQueue.isAvailableWaitingStatus(waitingQueue.waitingStatus());
    }

    /**
     * 대기열 활성 토큰 시간 초과 확인
     * @param seconds 기준 단위 (초)
     * @param tokenActiveAt 토큰 활성 시간
     * @return 토큰 활성 시간 이내 존재 여부
     */
    public boolean meetsIfActiveWaitingQueueInTimeLimits(Long seconds, Date tokenActiveAt) {
        Date currentDate = new Date();
        long targetSeconds = TimeUnit.MILLISECONDS.toSeconds(currentDate.getTime() - tokenActiveAt.getTime());
        return WaitingQueue.isInActiveDuration(seconds, targetSeconds);
    }

    /**
     * 대기열 진입
     * @param customerId 고객_ID
     * @return 대기열 진입 정보
     */
    @Transactional
    public WaitingQueue enterWaitingQueue(Long customerId) {
        WaitingQueue savedWaitingQueue = waitingRepository.saveWaitingQueue(WaitingQueue.create(null, customerId, (new Date()).toString() + customerId.toString(), WaitingStatus.ACTIVE, null, null));
        waitingRepository.saveWaitingEnterHistory(WaitingEnterHistory.create(null, savedWaitingQueue.waitingId(), null));
        return savedWaitingQueue;
    }

    /**
     * 대기 인원 조회
     * @return 대기 인원 수
     */
    public Long getTheNumberOfWaiting() {
        return waitingRepository.countWaitingQueueByWaitingStatus(WaitingStatus.ACTIVE);
    }

    /**
     * 대기 순번 조회
     * @param waitingId 대기열_ID
     * @return 대기 순번
     */
    public Long getWaitingNumberByWaitingId(Long waitingId) {
        long lastEnterId = waitingRepository.findOneWaitingEnterHistoryIdOrderByWaitingEnterHistoryIdDesc().orElse(0L);
        long currentEnterIdByWaitingId = waitingRepository.findOneWaitingEnterHistoryIdByWaitingIdOrderByWaitingEnterHistoryIdDesc(waitingId).orElse(0L);
        return Math.max(0L, lastEnterId - currentEnterIdByWaitingId);
    }

    /**
     * 대기열 토큰 만료 처리채ㅜ
     * @param waitingQueue 대기열 정보
     * @return 토큰 만료 처리 여부
     */
    public boolean expireWaitingQueueToken(WaitingQueue waitingQueue) {
        boolean isExpired = true;
        try {
            waitingRepository.saveWaitingQueue(WaitingQueue.create(waitingQueue.waitingId(), waitingQueue.customerId(), waitingQueue.token(), WaitingStatus.EXPIRED, waitingQueue.createdAt(), waitingQueue.deletedAt()));
            return isExpired;
        } catch (Exception e) {
            return !isExpired;
        }
    }

    /**
     * 만료된 토큰 모두 조회
     */
    public void removeAllExpiredWaitingQueueToken() {
        List<WaitingQueue> waitingQueues = waitingRepository.findAllExpiredWaitingQueue();
        Date now = new Date();
        for (WaitingQueue waitingQueue : waitingQueues) {
            Date createdAt = waitingQueue.createdAt();
            long duration = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - createdAt.getTime());
            if (duration > 300) {
                waitingRepository.saveWaitingQueue(WaitingQueue.create(waitingQueue.waitingId(), waitingQueue.customerId(), waitingQueue.token(), WaitingStatus.EXPIRED, waitingQueue.createdAt(), now));
            }
        }
    }
}
