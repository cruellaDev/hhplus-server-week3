package com.io.hhplus.concert.domain.queue;

import com.io.hhplus.concert.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class WaitingValidator {

    private final QueueTokenRepository waitingRepository;

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

    public boolean isInActiveDuration(Long seconds, Long targetSeconds) {
        return seconds != null && targetSeconds != null && seconds.compareTo(targetSeconds) > 0;
    }

    public boolean isNotInActiveDuration(Long seconds, Long targetSeconds) {
        return !this.isInActiveDuration(seconds, targetSeconds);
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

}
