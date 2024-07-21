package com.io.hhplus.concert.domain.waiting.service;

import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.enums.WaitingStatus;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.common.utils.DateUtils;
import com.io.hhplus.concert.domain.waiting.repository.WaitingRepository;
import com.io.hhplus.concert.domain.waiting.service.model.WaitingQueueModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WaitingValidator {

    private final WaitingRepository waitingRepository;

    public boolean isAvailableWaitingId(Long waitingId) {
        return waitingId != null && waitingId.compareTo(0L) > 0;
    }

    public boolean isNotAvailableWaitingId(Long waitingId) {
        return !this.isAvailableWaitingId(waitingId);
    }

    public boolean isAvailableToken(String token) {
        return token != null && !token.isBlank();
    }

    public boolean isNotAvailableToken(String token) {
        return !this.isAvailableToken(token);
    }

    public boolean isAvailableWaitingStatus(WaitingStatus waitingStatus) {
        return waitingStatus != null && waitingStatus.equals(WaitingStatus.ACTIVE);
    }

    public boolean isNotAvailableWaitingStatus(WaitingStatus waitingStatus) {
        return !this.isAvailableWaitingStatus(waitingStatus);
    }

    public boolean isInActiveDuration(Long seconds, Long targetSeconds) {
        return seconds != null && targetSeconds != null && seconds.compareTo(targetSeconds) > 0;
    }

    public boolean isNotInActiveDuration(Long seconds, Long targetSeconds) {
        return !this.isInActiveDuration(seconds, targetSeconds);
    }

    /**
     * 대기열 활성 토큰 존재 여부 확인
     * @param waitingQueueModel 대기열 정보
     * @return 대기열 활성 토큰 존재 여부
     */
    public boolean meetsIfActiveWaitingQueueExists(WaitingQueueModel waitingQueueModel) {
        return waitingQueueModel != null
                && this.isAvailableWaitingId(waitingQueueModel.waitingId())
                && this.isAvailableToken(waitingQueueModel.token())
                && this.isAvailableWaitingStatus(waitingQueueModel.waitingStatus());

    }

    /**
     * 대기열 활성 토큰 시간 초과 확인
     * @param seconds 기준 단위 (초)
     * @param tokenActiveAt 토큰 활성 시간
     * @return 토큰 활성 시간 이내 존재 여부
     */
    public boolean meetsIfActiveWaitingQueueInTimeLimits(Long seconds, Date tokenActiveAt) {
        if (seconds == null || tokenActiveAt == null) {
            return false;
        }
        Date currentDate = DateUtils.getSysDate();
        long targetSeconds = DateUtils.calculateDuration(currentDate, tokenActiveAt);
        return this.isInActiveDuration(seconds, targetSeconds);
    }

    /**
     * 토큰 검증
     * @param token 토큰 정보
     */
    public void validateToken(String token) {
        if (token == null) throw new CustomException(ResponseMessage.INVALID, "토큰 정보가 존재하지 않습니다.");
        Optional<WaitingQueueModel> waitingQueueModel = waitingRepository.findWaitingQueueByCustomerIdAndWaitingStatus(Long.valueOf(token), WaitingStatus.ACTIVE);
        if (waitingQueueModel.isEmpty()) throw new CustomException(ResponseMessage.WAITING_NOT_FOUND, "대기열 진입 정보가 존재하지 않습니다.");
        if (waitingQueueModel.get().waitingId().compareTo(0L) <= 0) throw new CustomException(ResponseMessage.WAITING_NOT_FOUND, "대기열 진입 정보가 존재하지 않습니다.");
    }
}
