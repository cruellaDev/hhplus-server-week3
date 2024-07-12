package com.io.hhplus.concert.application.waiting.facade;

import com.io.hhplus.concert.application.waiting.dto.WaitingResponse;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.enums.WaitingStatus;
import com.io.hhplus.concert.common.exceptions.IllegalArgumentCustomException;
import com.io.hhplus.concert.common.exceptions.ResourceNotFoundCustomException;
import com.io.hhplus.concert.domain.waiting.model.WaitingEnterHistory;
import com.io.hhplus.concert.domain.waiting.model.WaitingQueue;
import com.io.hhplus.concert.domain.waiting.repository.WaitingRepository;
import com.io.hhplus.concert.domain.waiting.service.WaitingService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
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

    @Autowired
    private WaitingService waitingService;

    @BeforeEach
    void setUp() {
        for (long i = 2; i < 10; i++) {
            WaitingQueue waitingQueue = waitingRepository.saveWaitingQueue(WaitingQueue.create(null, i, "userToken" + (new Date()).toString(), WaitingStatus.ACTIVE, null, null));
            waitingRepository.saveWaitingEnterHistory(WaitingEnterHistory.create(null, waitingQueue.waitingId(), null));
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
    void publishWaitingToken_when_valid_customer() {
        // given
        long customerId = 1;

        // when
        WaitingResponse result = waitingFacade.publishWaitingToken(customerId);

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
    void publishWaitingToken_when_customer_already_exists() {
        // given
        long customerId = 8;

        // when
        WaitingResponse result = waitingFacade.publishWaitingToken(customerId);

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
    void getWaitingToken_when_customer_not_exists() {
        // given
        long customerId = 99999;

        // when - then
        assertThatThrownBy(() -> waitingFacade.getWaitingToken(customerId))
                .isInstanceOf(ResourceNotFoundCustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_AVAILABLE);
    }

    /**
     * 대기열 토큰 조회 - 고객 데이터는 존재하나 대기열에 없는 고객
     */
    @Test
    void getWaitingToken_when_customer_not_waiting() {
        // given
        long customerId = 1;

        // when - then
        assertThatThrownBy(() -> waitingFacade.getWaitingToken(customerId))
                .isInstanceOf(ResourceNotFoundCustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_FOUND);
    }

    /**
     * 대기열 토큰 조회 - 이미 대기열에 존재하는 고객
     */
    @Test
    void getWaitingToken_when_customer_in_waiting() {
        // given
        long customerId = 8;

        // when
        WaitingResponse result = waitingFacade.getWaitingToken(customerId);

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
    void removeAllExpiredToken() {
        // given - when
        waitingFacade.removeAllExpiredToken();
        // then
        List<WaitingQueue> waitingQueues = waitingRepository.findAllExpiredWaitingQueue();
        assertTrue(waitingQueues.isEmpty());
    }
}