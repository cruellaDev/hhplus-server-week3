package com.io.hhplus.concert.domain.waiting.service;

import com.io.hhplus.concert.common.enums.WaitingStatus;
import com.io.hhplus.concert.common.utils.DateUtils;
import com.io.hhplus.concert.domain.waiting.model.WaitingEnterHistory;
import com.io.hhplus.concert.domain.waiting.model.WaitingQueue;
import com.io.hhplus.concert.domain.waiting.repository.WaitingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WaitingServiceTest {

    @Mock
    private WaitingRepository waitingRepository;

    @InjectMocks
    private WaitingService waitingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 고객의 활성화된 대기열 토큰 조회
     */
    @Test
    void getActiveWaitingTokenByCustomerId() {
        // given
        WaitingQueue waitingQueue = WaitingQueue.create(
                1L,
                1L,
                "userToken",
                WaitingStatus.ACTIVE,
                DateUtils.getSysDate(),
                null
        );
        given(waitingRepository.findWaitingQueueByCustomerIdAndWaitingStatus(anyLong(), any())).willReturn(Optional.of(waitingQueue));

        // when
        WaitingQueue result = waitingService.getActiveWaitingTokenByCustomerId(1L);

        // then
        assertAll(() -> assertEquals(waitingQueue.waitingId(), result.waitingId()),
                () -> assertEquals(waitingQueue.customerId(), result.customerId()),
                () -> assertEquals(waitingQueue.token(), result.token()),
                () -> assertEquals(waitingQueue.waitingStatus(), result.waitingStatus()),
                () -> assertEquals(waitingQueue.createdAt(), result.createdAt()));
    }

    /**
     * 대기열 정보 검증 모두 통과 시
     */
    @Test
    void meetsIfActiveWaitingQueueExists_when_pass_all() {
        // given
        WaitingQueue waitingQueue = WaitingQueue.create(
                1L,
                1L,
                "userToken",
                WaitingStatus.ACTIVE,
                DateUtils.getSysDate(),
                null
        );

        // when
        boolean isActive = waitingService.meetsIfActiveWaitingQueueExists(waitingQueue);

        // then
        assertThat(isActive).isTrue();
    }

    /**
     * 대기열 정보 검증 모두 대기열_ID 이상 시
     */
    @Test
    void meetsIfActiveWaitingQueueExists_when_waitingId_is_wrong() {
        // given
        WaitingQueue waitingQueue = WaitingQueue.create(
                -1L,
                1L,
                "userToken",
                WaitingStatus.ACTIVE,
                DateUtils.getSysDate(),
                null
        );

        // when
        boolean isActive = waitingService.meetsIfActiveWaitingQueueExists(waitingQueue);

        // then
        assertThat(isActive).isFalse();
    }

    /**
     * 대기열 정보 검증 모두 고객_ID 이상 시
     */
    @Test
    void meetsIfActiveWaitingQueueExists_when_customerId_is_wrong() {
        // given
        WaitingQueue waitingQueue = WaitingQueue.create(
                1L,
                -1L,
                "userToken",
                WaitingStatus.ACTIVE,
                DateUtils.getSysDate(),
                null
        );

        // when
        boolean isActive = waitingService.meetsIfActiveWaitingQueueExists(waitingQueue);

        // then
        assertThat(isActive).isFalse();
    }

    /**
     * 대기열 정보 검증 모두 토큰 값 이상 시
     */
    @Test
    void meetsIfActiveWaitingQueueExists_when_token_is_wrong() {
        // given
        WaitingQueue waitingQueue = WaitingQueue.create(
                1L,
                1L,
                "",
                WaitingStatus.ACTIVE,
                DateUtils.getSysDate(),
                null
        );

        // when
        boolean isActive = waitingService.meetsIfActiveWaitingQueueExists(waitingQueue);

        // then
        assertThat(isActive).isFalse();
    }

    /**
     * 대기열 정보 검증 모두 대기상태 이상 시
     */
    @Test
    void meetsIfActiveWaitingQueueExists_when_waitingStatus_is_wrong() {
        // given
        WaitingQueue waitingQueue = WaitingQueue.create(
                1L,
                1L,
                "userToken",
                WaitingStatus.EXPIRED,
                DateUtils.getSysDate(),
                null
        );

        // when
        boolean isActive = waitingService.meetsIfActiveWaitingQueueExists(waitingQueue);

        // then
        assertThat(isActive).isFalse();
    }

    /**
     * 대기열 활성 토큰 시간 초과 확인 - 시간 이내일 시
     */
    @Test
    void meetsIfActiveWaitingQueueInTimeLimits_when_in_duration() {
        // given
        Date tokenActiveAt = DateUtils.createTemporalDateByIntParameters(2024, 7, 12, 12, 36, 59);

        // when
        boolean isActive = waitingService.meetsIfActiveWaitingQueueInTimeLimits(300L, tokenActiveAt);

        // then
        assertThat(isActive).isTrue();
    }

    /**
     * 대기열 활성 토큰 시간 초과 확인 - 시간 이내일 시
     */
    @Test
    void meetsIfActiveWaitingQueueInTimeLimits_when_expired() {
        // given
        Date tokenActiveAt = DateUtils.createTemporalDateByIntParameters(2024, 7, 12, 12, 33, 59);

        // when
        boolean isActive = waitingService.meetsIfActiveWaitingQueueInTimeLimits(300L, tokenActiveAt);

        // then
        assertThat(isActive).isFalse();
    }

    /**
     * 대기열 진입
     */
    @Test
    void enterWaitingQueue() {
        // given
        WaitingQueue waitingQueue = WaitingQueue.create(
                1L,
                1L,
                "userToken",
                WaitingStatus.ACTIVE,
                DateUtils.getSysDate(),
                null
        );
        WaitingEnterHistory waitingEnterHistory = WaitingEnterHistory.create(
                1L,
                1L,
                DateUtils.getSysDate()
        );
        given(waitingRepository.saveWaitingQueue(any(WaitingQueue.class))).willReturn(waitingQueue);
        given(waitingRepository.saveWaitingEnterHistory(any(WaitingEnterHistory.class))).willReturn(waitingEnterHistory);

        // when
        WaitingQueue result = waitingService.enterWaitingQueue(1L);

        assertThat(result.waitingStatus()).isEqualTo(waitingQueue.waitingStatus());
    }

    /**
     * 대기 인원 조회
     */
    @Test
    void getTheNumberOfWaiting() {
        // given
        long numberOfWaiting = 50;
        given(waitingRepository.countWaitingQueueByWaitingStatus(any())).willReturn(numberOfWaiting);

        // when
        Long result = waitingService.getTheNumberOfWaiting();

        // then
        assertThat(result).isEqualTo(numberOfWaiting);
    }

    /**
     * 대기 순번 조회
     */
    @Test
    void getWaitingNumberByWaitingId() {
        // given
        long waitingNumber = 157;
        long lastEnterId = 160;
        long currentEnterIdByWaitingId = 3;
        given(waitingRepository.findOneWaitingEnterHistoryIdOrderByWaitingEnterHistoryIdDesc()).willReturn(Optional.of(lastEnterId));
        given(waitingRepository.findOneWaitingEnterHistoryIdByWaitingIdOrderByWaitingEnterHistoryIdDesc(anyLong())).willReturn(Optional.of(currentEnterIdByWaitingId));

        // when
        Long result = waitingService.getWaitingNumberByWaitingId(1L);

        // then
        assertThat(result).isEqualTo(waitingNumber);
    }

    /**
     * 대기열 토큰 만료 처리
     */
    @Test
    void expireWaitingQueueToken() {
        // given
        WaitingQueue asisWaitingQueue = WaitingQueue.create(
                1L,
                1L,
                "userToken",
                WaitingStatus.ACTIVE,
                DateUtils.getSysDate(),
                null
        );
        WaitingQueue tobeWaitingQueue = WaitingQueue.create(
                1L,
                1L,
                "userToken",
                WaitingStatus.EXPIRED,
                DateUtils.getSysDate(),
                null
        );
        given(waitingRepository.saveWaitingQueue(any(WaitingQueue.class))).willReturn(tobeWaitingQueue);

        // when
        boolean isExpired = waitingService.expireWaitingQueueToken(asisWaitingQueue);

        // then
        assertThat(isExpired).isTrue();
    }

    /**
     * 시간이 지나 만료됐거나 만료된 토큰 모두 제거
     */
    @Test
    void removeAllExpiredWaitingQueueToken() {
        // given
        List<WaitingQueue> asisWaitingQueues = List.of(
            WaitingQueue.create(
                    1L,
                    1L,
                    "userToken",
                    WaitingStatus.ACTIVE,
                    DateUtils.createTemporalDateByIntParameters(2024, 7, 12, 11, 23, 11),
                    null
            ),
            WaitingQueue.create(
                    2L,
                    2L,
                    "userToken",
                    WaitingStatus.EXPIRED,
                    DateUtils.createTemporalDateByIntParameters(2024, 7, 12, 11, 23, 11),
                    null
            )
        );
        // given
        List<WaitingQueue> tobeWaitingQueues = List.of(
                WaitingQueue.create(
                        1L,
                        1L,
                        "userToken",
                        WaitingStatus.ACTIVE,
                        DateUtils.createTemporalDateByIntParameters(2024, 7, 12, 11, 23, 11),
                        DateUtils.getSysDate()
                ),
                WaitingQueue.create(
                        2L,
                        2L,
                        "userToken",
                        WaitingStatus.EXPIRED,
                        DateUtils.createTemporalDateByIntParameters(2024, 7, 12, 11, 23, 11),
                        DateUtils.getSysDate()
                )
        );
        given(waitingRepository.findAllExpiredWaitingQueue()).willReturn(asisWaitingQueues);
        given(waitingRepository.saveWaitingQueue(any(WaitingQueue.class))).willReturn(tobeWaitingQueues.get(0));
        given(waitingRepository.saveWaitingQueue(any(WaitingQueue.class))).willReturn(tobeWaitingQueues.get(1));

        // when
        List<WaitingQueue> result = waitingService.removeAllExpiredWaitingQueueToken();

        // then
        assertAll(() -> assertEquals(tobeWaitingQueues.get(0).deletedAt(), result.get(0).deletedAt()));
        assertAll(() -> assertEquals(tobeWaitingQueues.get(1).deletedAt(), result.get(1).deletedAt()));
    }
}