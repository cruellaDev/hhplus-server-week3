package com.io.hhplus.concert.application.waiting.facade;

import com.io.hhplus.concert.application.waiting.dto.WaitingResultInfo;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.enums.WaitingStatus;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.domain.waiting.service.model.WaitingEnterHistoryModel;
import com.io.hhplus.concert.domain.waiting.service.model.WaitingQueueModel;
import com.io.hhplus.concert.domain.waiting.repository.WaitingRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WaitingIntegrationTest {

    @Autowired
    private WaitingFacade waitingFacade;

    @Autowired
    private WaitingRepository waitingRepository;

    @BeforeEach
    void setUp() {
        for (long i = 2; i < 10; i++) {
            WaitingQueueModel waitingQueueModel = waitingRepository.saveWaitingQueue(WaitingQueueModel.create(null, i, "userToken" + (new Date()).toString(), WaitingStatus.ACTIVE, null, null));
            waitingRepository.saveWaitingEnterHistory(WaitingEnterHistoryModel.create(null, waitingQueueModel.waitingId(), null));
        }
    }

    @AfterEach
    void tearDown() {
        waitingRepository.deleteAllWaitingQueue();
        waitingRepository.deleteAllWaitingEnterHistory();
    }

    /**
     * 대기열 토큰 발급 (대기열 진입) - 유효하고 대기열에 없는 고객
     */
    @Test
    void 대기열_토큰_발급_유효한_고객이지만_대기열에_없을_시_토큰_발급() {
        // given
        long customerId = 1;

        // when
        WaitingResultInfo result = waitingFacade.publishWaitingToken(customerId);

        // then
        assertAll(() -> assertEquals(1, result.getCustomerId()),
                () -> assertEquals(9, result.getNumberOfWaiting()),
                () -> assertEquals(9, result.getWaitingNumber()),
                () -> assertEquals(WaitingStatus.ACTIVE, result.getWaitingStatus()));
    }

    /**
     * 대기열 토큰 발급 (대기열 진입) - 이미 대기열에 존재하는 고객
     */
    @Test
    void 대기열_토큰_발급_유효한_고객이고_이미_대기열에_존재할_시_기존_토큰_반환() {
        // given
        long customerId = 8;

        // when
        WaitingResultInfo result = waitingFacade.publishWaitingToken(customerId);

        // then
        assertAll(() -> assertEquals(8, result.getCustomerId()),
                () -> assertEquals(8, result.getNumberOfWaiting()),
                () -> assertEquals(7, result.getWaitingNumber()),
                () -> assertEquals(WaitingStatus.ACTIVE, result.getWaitingStatus()));
    }

    /**
     * 대기열 토큰 조회 - 존재하지 않는 고객
     */
    @Test
    void 대기열_토큰_조회_존재하지_않는_고객일_시_예외_처리() {
        // given
        long customerId = 99999;

        // when - then
        assertThatThrownBy(() -> waitingFacade.getWaitingToken(customerId))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_AVAILABLE);
    }

    /**
     * 대기열 토큰 조회 - 고객 데이터는 존재하나 대기열에 없는 고객
     */
    @Test
    void 대기열_토큰_조회_유효한_고객이지만_대기열에_없을_시_예외_처리() {
        // given
        long customerId = 1;

        // when - then
        assertThatThrownBy(() -> waitingFacade.getWaitingToken(customerId))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_FOUND);
    }

    /**
     * 대기열 토큰 조회 - 이미 대기열에 존재하는 고객
     */
    @Test
    void 대기열_토큰_조회_대기열에_존재하는_고객일_시_토큰_반환() {
        // given
        long customerId = 8;

        // when
        WaitingResultInfo result = waitingFacade.getWaitingToken(customerId);

        // then
        assertAll(() -> assertEquals(8, result.getCustomerId()),
                () -> assertEquals(8, result.getNumberOfWaiting()),
                () -> assertEquals(7, result.getWaitingNumber()),
                () -> assertEquals(WaitingStatus.ACTIVE, result.getWaitingStatus()));
    }

    /**
     * 만료 토큰 제거
     */
    @Test
    void 만료된_토큰_제거_성공() {
        // given - when
        waitingFacade.removeAllExpiredToken();
        // then
        List<WaitingQueueModel> waitingQueueModels = waitingRepository.findAllExpiredWaitingQueue();
        assertTrue(waitingQueueModels.isEmpty());
    }
}