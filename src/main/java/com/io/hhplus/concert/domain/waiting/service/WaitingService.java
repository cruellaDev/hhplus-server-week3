package com.io.hhplus.concert.domain.waiting.service;

import com.io.hhplus.concert.common.enums.WaitingStatus;
import com.io.hhplus.concert.common.utils.DateUtils;
import com.io.hhplus.concert.domain.customer.service.CustomerValidator;
import com.io.hhplus.concert.domain.waiting.service.model.WaitingEnterHistoryModel;
import com.io.hhplus.concert.domain.waiting.service.model.WaitingQueueModel;
import com.io.hhplus.concert.domain.waiting.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final WaitingValidator waitingValidator;
    private final CustomerValidator customerValidator;

    /**
     * 고객의 활성화된 대기열 토큰 조회
     * @param customerId 고객_ID
     * @return 대기열 토큰 정보
     */
    public WaitingQueueModel getActiveWaitingTokenByCustomerId(Long customerId) {
        return waitingRepository.findWaitingQueueByCustomerIdAndWaitingStatus(customerId, WaitingStatus.ACTIVE).orElseGet(WaitingQueueModel::noContents);
    }

    /**
     * 대기열 진입
     * @param customerId 고객_ID
     * @return 대기열 진입 정보
     */
    @Transactional
    public WaitingQueueModel enterWaitingQueue(Long customerId) {
        WaitingQueueModel savedWaitingQueueModel = waitingRepository.saveWaitingQueue(WaitingQueueModel.create(null, customerId, (new Date()).toString() + customerId.toString(), WaitingStatus.ACTIVE, null, null));
        waitingRepository.saveWaitingEnterHistory(WaitingEnterHistoryModel.create(null, savedWaitingQueueModel.waitingId(), null));
        return savedWaitingQueueModel;
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
        long firstEnterId = waitingRepository.findOneWaitingEnterHistoryIdOrderByWaitingEnterHistoryIdAsc().orElse(0L);
        long currentEnterIdByWaitingId = waitingRepository.findOneWaitingEnterHistoryIdByWaitingIdOrderByWaitingEnterHistoryIdDesc(waitingId).orElse(0L);
        return Math.max(0L, currentEnterIdByWaitingId - firstEnterId + 1);
    }

    /**
     * 대기열 토큰 만료 처리
     * @param waitingQueueModel 대기열 정보
     * @return 토큰 만료 처리 여부
     */
    public boolean expireWaitingQueueToken(WaitingQueueModel waitingQueueModel) {
        boolean isExpired = true;
        try {
            waitingRepository.saveWaitingQueue(WaitingQueueModel.create(waitingQueueModel.waitingId(), waitingQueueModel.customerId(), waitingQueueModel.token(), WaitingStatus.EXPIRED, waitingQueueModel.createdAt(), waitingQueueModel.deletedAt()));
            return isExpired;
        } catch (Exception e) {
            return !isExpired;
        }
    }

    /**
     * 시간이 지나 만료됐거나 만료된 토큰 모두 제거
     */
    public List<WaitingQueueModel> removeAllExpiredWaitingQueueToken() {
        List<WaitingQueueModel> waitingQueueModels = waitingRepository.findAllExpiredWaitingQueue();
        Date currentDate = DateUtils.getSysDate();
        long durationLimit = 300;
        return waitingQueueModels
                .stream()
                .filter(waitingQueueModel -> {
                    long duration = DateUtils.calculateDuration(currentDate, waitingQueueModel.createdAt());
                    return duration > durationLimit;
                })
                .map(waitingQueueModel -> waitingRepository.saveWaitingQueue(WaitingQueueModel.create(waitingQueueModel.waitingId(), waitingQueueModel.customerId(), waitingQueueModel.token(), WaitingStatus.EXPIRED, waitingQueueModel.createdAt(), currentDate)))
                .collect(Collectors.toList());
    }
}
