package com.io.hhplus.concert.domain.payment.service;

import com.io.hhplus.concert.common.utils.DateUtils;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Disabled
class ReservationValidatorTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationValidator reservationValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void isAvailableReservationId_reservationId_is_null() {
        // given
        Long reservationId = null;

        // when
        boolean isValid = reservationValidator.isAvailableReservationId(reservationId);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableReservationId_reservationId_is_negative() {
        // given
        Long reservationId = -1L;

        // when
        boolean isValid = reservationValidator.isAvailableReservationId(reservationId);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableReservationId() {
        // given
        Long reservationId = 1L;

        // when
        boolean isValid = reservationValidator.isAvailableReservationId(reservationId);

        // then
        assertTrue(isValid);
    }

    @Test
    void isAvailableReserverName_reserverName_is_null() {
        // given
        String reserverName = null;

        // when
        boolean isValid = reservationValidator.isAvailableReceiverName(reserverName);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableReserverName_reserverName_is_blank() {
        // given
        String reserverName = " ";

        // when
        boolean isValid = reservationValidator.isAvailableReceiverName(reserverName);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableReserverName() {
        // given
        String reserverName = "예매자명";

        // when
        boolean isValid = reservationValidator.isAvailableReceiverName(reserverName);

        // then
        assertTrue(isValid);
    }

    @Test
    void isAvailableReceiverName_is_null() {
        // given
        String receiverName = null;

        // when
        boolean isValid = reservationValidator.isAvailableReceiverName(receiverName);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableReceiverName_is_blank() {
        // given
        String receiverName = " ";

        // when
        boolean isValid = reservationValidator.isAvailableReceiverName(receiverName);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableReceiverName() {
        // given
        String receiverName = "수령인명";

        // when
        boolean isValid = reservationValidator.isAvailableReceiverName(receiverName);

        // then
        assertTrue(isValid);
    }

    @Test
    void isAvailableReceiveMethod_is_null() {
        // given
        ReceiveMethod receiveMethod = null;

        // when
        boolean isValid = reservationValidator.isAvailableReceiveMethod(receiveMethod);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableReceiveMethod() {
        // given
        ReceiveMethod receiveMethod = ReceiveMethod.ONLINE;

        // when
        boolean isValid = reservationValidator.isAvailableReceiveMethod(receiveMethod);

        // then
        assertTrue(isValid);
    }

    @Test
    void isAvailableReservationStatus_is_null() {
        // given
        ReservationStatus reservationStatus = null;

        // when
        boolean isValid = reservationValidator.isAvailableReservationStatus(reservationStatus);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableReservationStatus() {
        // given
        ReservationStatus reservationStatus = ReservationStatus.REQUESTED;

        // when
        boolean isValid = reservationValidator.isAvailableReservationStatus(reservationStatus);

        // then
        assertTrue(isValid);
    }

    @Test
    void isAbleToPayReservationStatus_is_null() {
        // given
        ReservationStatus reservationStatus = null;

        // when
        boolean isValid = reservationValidator.isAbleToPayReservationStatus(reservationStatus);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAbleToPayReservationStatus_is_not_requested() {
        // given
        ReservationStatus reservationStatus = ReservationStatus.COMPLETED;

        // when
        boolean isValid = reservationValidator.isAbleToPayReservationStatus(reservationStatus);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAbleToPayReservationStatus_is_requested() {
        // given
        ReservationStatus reservationStatus = ReservationStatus.REQUESTED;

        // when
        boolean isValid = reservationValidator.isAbleToPayReservationStatus(reservationStatus);

        // then
        assertTrue(isValid);
    }

    @Test
    void isInPayableDuration_args_are_null() {
        // given
        Long seconds = null;
        Long targetSeconds = null;

        // when
        boolean isValid = reservationValidator.isInPayableDuration(seconds, targetSeconds);

        // then
        assertFalse(isValid);
    }

    @Test
    void isInPayableDuration_not_in_duration() {
        // given
        Long seconds = 300L;
        Long targetSeconds = 500L;

        // when
        boolean isValid = reservationValidator.isInPayableDuration(seconds, targetSeconds);

        // then
        assertFalse(isValid);
    }

    @Test
    void isInPayableDuration_in_duration() {
        // given
        Long seconds = 300L;
        Long targetSeconds = 100L;

        // when
        boolean isValid = reservationValidator.isInPayableDuration(seconds, targetSeconds);

        // then
        assertTrue(isValid);
    }

    @Test
    void meetsIfAbleToPayInTimeLimits_in_timeLimits() {
        // given
        Long seconds = 300L;
        Date reservationStatusChangedAt = DateUtils.getSysDate();

        // when
        boolean isValid = reservationValidator.meetsIfAbleToPayInTimeLimits(seconds, reservationStatusChangedAt);

        // then
        assertTrue(isValid);
    }

    @Test
    void meetsIfAbleToPayInTimeLimits_not_in_timeLimits() {
        // given
        Long seconds = 300L;
        Date reservationStatusChangedAt = DateUtils.createTemporalDateByIntParameters(2024,7,18,23,59,59);

        // when
        boolean isValid = reservationValidator.meetsIfAbleToPayInTimeLimits(seconds, reservationStatusChangedAt);

        // then
        assertFalse(isValid);
    }

    @Test
    void meetsIfReservationAvailable_reservationId_is_wrong() {
        // given
        ReservationModel reservationModel = ReservationModel
                .create(-1L, 1L, "예매자명", ReservationStatus.REQUESTED, DateUtils.getSysDate(), ReceiveMethod.ONLINE, "수령인명", null, null, null);

        // when
        boolean isValid = reservationValidator.meetsIfReservationAvailable(reservationModel);

        // then
        assertFalse(isValid);
    }

    @Test
    void meetsIfReservationAvailable_reserverName_is_wrong() {
        // given
        ReservationModel reservationModel = ReservationModel
                .create(1L, 1L, "", ReservationStatus.REQUESTED, DateUtils.getSysDate(), ReceiveMethod.ONLINE, "수령인명", null, null, null);

        // when
        boolean isValid = reservationValidator.meetsIfReservationAvailable(reservationModel);

        // then
        assertFalse(isValid);
    }

    @Test
    void meetsIfReservationAvailable_reservationStatus_is_wrong() {
        // given
        ReservationModel reservationModel = ReservationModel
                .create(1L, 1L, "예매자명", null, DateUtils.getSysDate(), ReceiveMethod.ONLINE, "수령인명", null, null, null);

        // when
        boolean isValid = reservationValidator.meetsIfReservationAvailable(reservationModel);

        // then
        assertFalse(isValid);
    }

    @Test
    void meetsIfReservationAvailable_receiveMethod_is_wrong() {
        // given
        ReservationModel reservationModel = ReservationModel
                .create(1L, 1L, "예매자명", ReservationStatus.CANCELLED, DateUtils.getSysDate(), null, "수령인명", null, null, null);

        // when
        boolean isValid = reservationValidator.meetsIfReservationAvailable(reservationModel);

        // then
        assertFalse(isValid);
    }

    @Test
    void meetsIfReservationAvailable_receiveMethod_success() {
        ReservationModel reservationModel = ReservationModel
                .create(1L, 1L, "예매자명", ReservationStatus.CANCELLED, DateUtils.getSysDate(), ReceiveMethod.ONLINE, "수령인명", null, null, null);

        // when
        boolean isValid = reservationValidator.meetsIfReservationAvailable(reservationModel);

        // then
        assertTrue(isValid);
    }
}