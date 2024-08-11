package com.io.hhplus.concert.domain.service;

import com.io.hhplus.concert.domain.queue.QueueTokenRepository;
import com.io.hhplus.concert.domain.queue.TokenService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Disabled
class WaitingServiceTest {

    @Mock
    private QueueTokenRepository waitingRepository;

    @InjectMocks
    private TokenService waitingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 고객의 활성화된 대기열 토큰 조회
     */
//    @Test
//    void getActiveWaitingTokenByCustomerId() {
//        // given
//        WaitingQueueModel waitingQueueModel = WaitingQueueModel.create(
//                1L,
//                1L,
//                "userToken",
//                WaitingStatus.ACTIVE,
//                DateUtils.getSysDate(),
//                null
//        );
//        given(waitingRepository.findWaitingQueueByCustomerIdAndWaitingStatus(anyLong(), any())).willReturn(Optional.of(waitingQueueModel));
//
//        // when
//        WaitingQueueModel result = waitingService.getActiveWaitingTokenByCustomerId(1L);
//
//        // then
//        assertAll(() -> assertEquals(waitingQueueModel.waitingId(), result.waitingId()),
//                () -> assertEquals(waitingQueueModel.customerId(), result.customerId()),
//                () -> assertEquals(waitingQueueModel.token(), result.token()),
//                () -> assertEquals(waitingQueueModel.waitingStatus(), result.waitingStatus()),
//                () -> assertEquals(waitingQueueModel.createdAt(), result.createdAt()));
//    }
//
//    /**
//     * 대기열 진입
//     */
//    @Test
//    void enterWaitingQueue() {
//        // given
//        WaitingQueueModel waitingQueueModel = WaitingQueueModel.create(
//                1L,
//                1L,
//                "userToken",
//                WaitingStatus.ACTIVE,
//                DateUtils.getSysDate(),
//                null
//        );
//        WaitingEnterHistoryModel waitingEnterHistoryModel = WaitingEnterHistoryModel.create(
//                1L,
//                1L,
//                DateUtils.getSysDate()
//        );
//        given(waitingRepository.saveWaitingQueue(any(WaitingQueueModel.class))).willReturn(waitingQueueModel);
//        given(waitingRepository.saveWaitingEnterHistory(any(WaitingEnterHistoryModel.class))).willReturn(waitingEnterHistoryModel);
//
//        // when
//        WaitingQueueModel result = waitingService.enterWaitingQueue(1L);
//
//        assertThat(result.waitingStatus()).isEqualTo(waitingQueueModel.waitingStatus());
//    }
//
//    /**
//     * 대기 인원 조회
//     */
//    @Test
//    void getTheNumberOfWaiting() {
//        // given
//        long numberOfWaiting = 50;
//        given(waitingRepository.countWaitingQueueByWaitingStatus(any())).willReturn(numberOfWaiting);
//
//        // when
//        Long result = waitingService.getTheNumberOfWaiting();
//
//        // then
//        assertThat(result).isEqualTo(numberOfWaiting);
//    }
//
//    /**
//     * 대기 순번 조회
//     */
//    @Test
//    void getWaitingNumberByWaitingId() {
//        // given
//        long waitingNumber = 157;
//        long lastEnterId = 160;
//        long currentEnterIdByWaitingId = 3;
//        given(waitingRepository.findOneWaitingEnterHistoryIdOrderByWaitingEnterHistoryIdAsc()).willReturn(Optional.of(lastEnterId));
//        given(waitingRepository.findOneWaitingEnterHistoryIdByWaitingIdOrderByWaitingEnterHistoryIdDesc(anyLong())).willReturn(Optional.of(currentEnterIdByWaitingId));
//
//        // when
//        Long result = waitingService.getWaitingNumberByWaitingId(1L);
//
//        // then
//        assertThat(result).isEqualTo(waitingNumber);
//    }
//
//    /**
//     * 대기열 토큰 만료 처리
//     */
//    @Test
//    void expireWaitingQueueToken() {
//        // given
//        WaitingQueueModel asisWaitingQueueModel = WaitingQueueModel.create(
//                1L,
//                1L,
//                "userToken",
//                WaitingStatus.ACTIVE,
//                DateUtils.getSysDate(),
//                null
//        );
//        WaitingQueueModel tobeWaitingQueueModel = WaitingQueueModel.create(
//                1L,
//                1L,
//                "userToken",
//                WaitingStatus.EXPIRED,
//                DateUtils.getSysDate(),
//                null
//        );
//        given(waitingRepository.saveWaitingQueue(any(WaitingQueueModel.class))).willReturn(tobeWaitingQueueModel);
//
//        // when
//        boolean isExpired = waitingService.expireWaitingQueueToken(asisWaitingQueueModel);
//
//        // then
//        assertThat(isExpired).isTrue();
//    }
//
//    /**
//     * 시간이 지나 만료됐거나 만료된 토큰 모두 제거
//     */
//    @Test
//    void removeAllExpiredWaitingQueueToken() {
//        // given
//        List<WaitingQueueModel> asisWaitingQueueModels = List.of(
//            WaitingQueueModel.create(
//                    1L,
//                    1L,
//                    "userToken",
//                    WaitingStatus.ACTIVE,
//                    DateUtils.createTemporalDateByIntParameters(2024, 7, 12, 11, 23, 11),
//                    null
//            ),
//            WaitingQueueModel.create(
//                    2L,
//                    2L,
//                    "userToken",
//                    WaitingStatus.EXPIRED,
//                    DateUtils.createTemporalDateByIntParameters(2024, 7, 12, 11, 23, 11),
//                    null
//            )
//        );
//        // given
//        List<WaitingQueueModel> tobeWaitingQueueModels = List.of(
//                WaitingQueueModel.create(
//                        1L,
//                        1L,
//                        "userToken",
//                        WaitingStatus.ACTIVE,
//                        DateUtils.createTemporalDateByIntParameters(2024, 7, 12, 11, 23, 11),
//                        DateUtils.getSysDate()
//                ),
//                WaitingQueueModel.create(
//                        2L,
//                        2L,
//                        "userToken",
//                        WaitingStatus.EXPIRED,
//                        DateUtils.createTemporalDateByIntParameters(2024, 7, 12, 11, 23, 11),
//                        DateUtils.getSysDate()
//                )
//        );
//        given(waitingRepository.findAllExpiredWaitingQueue()).willReturn(asisWaitingQueueModels);
//        given(waitingRepository.saveWaitingQueue(any(WaitingQueueModel.class))).willReturn(tobeWaitingQueueModels.get(0));
//        given(waitingRepository.saveWaitingQueue(any(WaitingQueueModel.class))).willReturn(tobeWaitingQueueModels.get(1));
//
//        // when
//        List<WaitingQueueModel> result = waitingService.removeAllExpiredWaitingQueueToken();
//
//        // then
//        assertAll(() -> assertEquals(tobeWaitingQueueModels.get(0).deletedAt(), result.get(0).deletedAt()));
//        assertAll(() -> assertEquals(tobeWaitingQueueModels.get(1).deletedAt(), result.get(1).deletedAt()));
//    }
}