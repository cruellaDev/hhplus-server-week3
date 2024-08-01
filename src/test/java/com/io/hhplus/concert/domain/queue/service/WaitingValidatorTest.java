package com.io.hhplus.concert.domain.queue.service;

import com.io.hhplus.concert.domain.queue.QueueTokenRepository;
import com.io.hhplus.concert.domain.queue.WaitingValidator;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Disabled
class WaitingValidatorTest {

    @Mock
    private QueueTokenRepository waitingRepository;

    @InjectMocks
    private WaitingValidator waitingValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void isAvailableWaitingId() {
        // given
        long waitingId = 1;

        // when
        boolean result = waitingValidator.isAvailableWaitingId(waitingId);

        // then
        assertTrue(result);
    }

    @Test
    void isAvailableWaitingId_waitingId_is_null() {
        // given
        Long waitingId = null;

        // when
        boolean result = waitingValidator.isAvailableWaitingId(waitingId);

        // then
        assertFalse(result);
    }

    @Test
    void isAvailableWaitingId_waitingId_is_negative() {
        // given
        long waitingId = -1;

        // when
        boolean result = waitingValidator.isAvailableWaitingId(waitingId);

        // then
        assertFalse(result);
    }

    @Test
    void isAvailableToken() {
        // given
        String token = UUID.randomUUID().toString();

        // when
        boolean result = waitingValidator.isAvailableToken(token);

        // then
        assertTrue(result);
    }

    @Test
    void isAvailableToken_is_null() {
        // given
        String token = null;

        // when
        boolean result = waitingValidator.isAvailableToken(token);

        // then
        assertFalse(result);
    }

    @Test
    void isAvailableToken_is_blank() {
        // given
        String token = " ";

        // when
        boolean result = waitingValidator.isAvailableToken(token);

        // then
        assertFalse(result);
    }

//    @Test
//    void isAvailableWaitingStatus() {
//        // given
//        WaitingStatus waitingStatus = WaitingStatus.ACTIVE;
//
//        // when
//        boolean result = waitingValidator.isAvailableWaitingStatus(waitingStatus);
//
//        // then
//        assertTrue(result);
//    }
//
//    @Test
//    void isAvailableWaitingStatus_is_null() {
//        // given
//        WaitingStatus waitingStatus = null;
//
//        // when
//        boolean result = waitingValidator.isAvailableWaitingStatus(waitingStatus);
//
//        // then
//        assertFalse(result);
//    }
//
//    @Test
//    void isAvailableWaitingStatus_is_not_active() {
//        // given
//        WaitingStatus waitingStatus = WaitingStatus.EXPIRED;
//
//        // when
//        boolean result = waitingValidator.isAvailableWaitingStatus(waitingStatus);
//
//        // then
//        assertFalse(result);
//    }
//
//    @Test
//    void isInActiveDuration() {
//        // given
//        long seconds = 300;
//        long targetSeconds = 100;
//
//        // when
//        boolean result = waitingValidator.isInActiveDuration(seconds, targetSeconds);
//
//        // then
//        assertTrue(result);
//    }
//
//    @Test
//    void isInActiveDuration_args_are_null() {
//        // given
//        Long seconds = null;
//        Long targetSeconds = null;
//
//        // when
//        boolean result = waitingValidator.isInActiveDuration(seconds, targetSeconds);
//
//        // then
//        assertFalse(result);
//    }
//
//    @Test
//    void isInActiveDuration_over_duration() {
//        // given
//        long seconds = 100;
//        long targetSeconds = 300;
//
//        // when
//        boolean result = waitingValidator.isInActiveDuration(seconds, targetSeconds);
//
//        // then
//        assertFalse(result);
//    }
//
//    @Test
//    void meetsIfActiveWaitingQueueExists() {
//        // given
//        WaitingQueueModel waitingQueueModel = WaitingQueueModel.create(1L, 1L, UUID.randomUUID().toString(), WaitingStatus.ACTIVE, null, null);
//
//        // when
//        boolean isActive = waitingValidator.meetsIfActiveWaitingQueueExists(waitingQueueModel);
//
//        // then
//        assertTrue(isActive);
//    }
//
//    @Test
//    void meetsIfActiveWaitingQueueExists_waitingId_is_wrong() {
//        // given
//        WaitingQueueModel waitingQueueModel = WaitingQueueModel.create(-1L, 1L, UUID.randomUUID().toString(), WaitingStatus.ACTIVE, null, null);
//
//        // when
//        boolean isActive = waitingValidator.meetsIfActiveWaitingQueueExists(waitingQueueModel);
//
//        // then
//        assertFalse(isActive);
//    }
//
//    @Test
//    void meetsIfActiveWaitingQueueExists_token_is_wrong() {
//        // given
//        WaitingQueueModel waitingQueueModel = WaitingQueueModel.create(1L, 1L, null, WaitingStatus.ACTIVE, null, null);
//
//        // when
//        boolean isActive = waitingValidator.meetsIfActiveWaitingQueueExists(waitingQueueModel);
//
//        // then
//        assertFalse(isActive);
//    }
//
//    @Test
//    void meetsIfActiveWaitingQueueExists_waitingStatus_is_wrong() {
//        // given
//        WaitingQueueModel waitingQueueModel = WaitingQueueModel.create(1L, 1L, UUID.randomUUID().toString(), WaitingStatus.EXPIRED, null, null);
//
//        // when
//        boolean isActive = waitingValidator.meetsIfActiveWaitingQueueExists(waitingQueueModel);
//
//        // then
//        assertFalse(isActive);
//    }
//
//    @Test
//    void meetsIfActiveWaitingQueueExists_waitingModel_is_null() {
//        // given
//        WaitingQueueModel waitingQueueModel = null;
//
//        // when
//        boolean isActive = waitingValidator.meetsIfActiveWaitingQueueExists(waitingQueueModel);
//
//        // then
//        assertFalse(isActive);
//    }
//
//    @Test
//    void meetsIfActiveWaitingQueueInTimeLimits_token_is_over() {
//        // given
//        long seconds = 300;
//        Date tokenActiveAt = DateUtils.createTemporalDateByIntParameters(2023, 11, 11, 11, 11, 11);
//
//        // when
//        boolean isInDuration = waitingValidator.meetsIfActiveWaitingQueueInTimeLimits(seconds, tokenActiveAt);
//
//        // then
//        assertFalse(isInDuration);
//    }
//
//    @Test
//    void meetsIfActiveWaitingQueueInTimeLimits_args_are_null() {
//        // given
//        Long seconds = null;
//        Date tokenActiveAt = null;
//
//        // when
//        boolean isInDuration = waitingValidator.meetsIfActiveWaitingQueueInTimeLimits(seconds, tokenActiveAt);
//
//        // then
//        assertFalse(isInDuration);
//    }
//
//    @Test
//    void validateToken_when_data_is_empty_then_throw() {
//        // given
//        String token = "1";
//        given(waitingRepository.findWaitingQueueByCustomerIdAndWaitingStatus(anyLong(), any())).willReturn(Optional.of(WaitingQueueModel.noContents()));
//
//        // when
//        assertThrows(CustomException.class, () -> waitingValidator.validateToken(token));
//    }
//
//    @Test
//    void validateToken_success() {
//        // given
//        String token = "2";
//        WaitingQueueModel waitingQueueModel = WaitingQueueModel.create(1L, 1L, UUID.randomUUID().toString(), WaitingStatus.ACTIVE, null, null);
//        given(waitingRepository.findWaitingQueueByCustomerIdAndWaitingStatus(anyLong(), any())).willReturn(Optional.of(waitingQueueModel));
//
//        // when
//        waitingValidator.validateToken(token);
//
//        // then
//        assertTrue(true);
//    }
}