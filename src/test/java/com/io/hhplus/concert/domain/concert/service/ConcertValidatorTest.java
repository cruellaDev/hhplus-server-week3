package com.io.hhplus.concert.domain.concert.service;

import com.io.hhplus.concert.common.enums.ConcertStatus;
import com.io.hhplus.concert.common.enums.SeatStatus;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.common.utils.DateUtils;
import com.io.hhplus.concert.domain.concert.repository.ConcertRepository;
import com.io.hhplus.concert.domain.concert.repository.PerformanceRepository;
import com.io.hhplus.concert.domain.concert.repository.SeatRepository;
import com.io.hhplus.concert.domain.concert.service.model.ConcertModel;
import com.io.hhplus.concert.domain.concert.service.model.PerformanceModel;
import com.io.hhplus.concert.domain.concert.service.model.SeatModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ConcertValidatorTest {

    @Mock
    private ConcertRepository concertRepository;

    @Mock
    private PerformanceRepository performanceRepository;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private ConcertValidator concertValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void isAvailableConcertId_concertId_is_null() {
        // given
        Long concertId = null;

        // when
        boolean isValid = concertValidator.isAvailableConcertId(concertId);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableConcertId_concertId_is_negative() {
        // given
        Long concertId = -1L;

        // when
        boolean isValid = concertValidator.isAvailableConcertId(concertId);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableConcertId() {
        // given
        Long concertId = 1L;

        // when
        boolean isValid = concertValidator.isAvailableConcertId(concertId);

        // then
        assertTrue(isValid);
    }

    @Test
    void isAvailableConcertName_is_null() {
        // given
        String concertName = null;

        // when
        boolean isValid = concertValidator.isAvailableConcertName(concertName);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableConcertName_is_blank() {
        // given
        String concertName = " ";

        // when
        boolean isValid = concertValidator.isAvailableConcertName(concertName);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableConcertName() {
        // given
        String concertName = "항해콘서트";

        // when
        boolean isValid = concertValidator.isAvailableConcertName(concertName);

        // then
        assertTrue(isValid);
    }

    @Test
    void isAvailableStatus_is_null() {
        // given
        ConcertStatus concertStatus = null;

        // when
        boolean isValid = concertValidator.isAvailableStatus(concertStatus);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableStatus_not_available() {
        // given
        ConcertStatus concertStatus = ConcertStatus.CLOSED;

        // when
        boolean isValid = concertValidator.isAvailableStatus(concertStatus);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableStatus() {
        // given
        ConcertStatus concertStatus = ConcertStatus.AVAILABLE;

        // when
        boolean isValid = concertValidator.isAvailableStatus(concertStatus);

        // then
        assertTrue(isValid);
    }

    @Test
    void isAvailablePerformanceId_is_null() {
        // given
        Long performanceId = null;

        // when
        boolean isValid = concertValidator.isAvailablePerformanceId(performanceId);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailablePerformanceId_is_negative() {
        // given
        Long performanceId = -1L;

        // when
        boolean isValid = concertValidator.isAvailablePerformanceId(performanceId);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailablePerformanceId() {
        // given
        Long performanceId = 1L;

        // when
        boolean isValid = concertValidator.isAvailablePerformanceId(performanceId);

        // then
        assertTrue(isValid);
    }

    @Test
    void isAvailableCapacityLimit_is_null() {
        // given
        Integer capacityLimit = null;

        // when
        boolean isValid = concertValidator.isAvailableCapacityLimit(capacityLimit);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableCapacityLimit_is_negative() {
        // given
        Integer capacityLimit = -50;

        // when
        boolean isValid = concertValidator.isAvailableCapacityLimit(capacityLimit);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableCapacityLimit() {
        // given
        Integer capacityLimit = 50;

        // when
        boolean isValid = concertValidator.isAvailableCapacityLimit(capacityLimit);

        // then
        assertTrue(isValid);
    }

    @Test
    void isPerformAtAfterTargetDate_args_are_null() {
        // given
        Date performedAt = null;
        Date targetDate = null;

        // when
        boolean isValid = concertValidator.isPerformAtAfterTargetDate(performedAt, targetDate);

        // then
        assertFalse(isValid);
    }

    @Test
    void isPerformAtAfterTargetDate_before_targetDate() {
        // given
        Date performedAt = DateUtils.createTemporalDateByIntParameters(2024,1,1,1,1,1);
        Date targetDate = DateUtils.getSysDate();

        // when
        boolean isValid = concertValidator.isPerformAtAfterTargetDate(performedAt, targetDate);

        // then
        assertFalse(isValid);
    }

    @Test
    void isPerformAtAfterTargetDate() {
        // given
        Date performedAt = DateUtils.createTemporalDateByIntParameters(2025,1,1,1,1,1);
        Date targetDate = DateUtils.createTemporalDateByIntParameters(2024,1,1,1,1,1);

        // when
        boolean isValid = concertValidator.isPerformAtAfterTargetDate(performedAt, targetDate);

        // then
        assertTrue(isValid);
    }

    @Test
    void isAvailableSeatId_is_negate() {
        // given
        Long seatId = -1L;

        // when
        boolean isValid = concertValidator.isAvailableSeatId(seatId);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableSeatId_is_null() {
        // given
        Long seatId = null;

        // when
        boolean isValid = concertValidator.isAvailableSeatId(seatId);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableSeatId() {
        // given
        Long seatId = 1L;

        // when
        boolean isValid = concertValidator.isAvailableSeatId(seatId);

        // then
        assertTrue(isValid);
    }

    @Test
    void isAvailableSeatNo_is_null() {
        // given
        String seatNo = null;

        // when
        boolean isValid = concertValidator.isAvailableSeatNo(seatNo);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableSeatNo_is_blank() {
        // given
        String seatNo = " ";

        // when
        boolean isValid = concertValidator.isAvailableSeatNo(seatNo);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableSeatNo() {
        // given
        String seatNo = "50";

        // when
        boolean isValid = concertValidator.isAvailableSeatNo(seatNo);

        // then
        assertTrue(isValid);
    }

    @Test
    void isAvailableSeatStatus_is_null() {
        // given
        SeatStatus seatStatus = null;

        // when
        boolean isValid = concertValidator.isAvailableSeatStatus(seatStatus);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableSeatStatus_not_available() {
        // given
        SeatStatus seatStatus = SeatStatus.NOT_AVAILABLE;

        // when
        boolean isValid = concertValidator.isAvailableSeatStatus(seatStatus);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailableSeatStatus() {
        // given
        SeatStatus seatStatus = SeatStatus.AVAILABLE;

        // when
        boolean isValid = concertValidator.isAvailableSeatStatus(seatStatus);

        // then
        assertTrue(isValid);
    }

    @Test
    void isOccupied_not_occupied() {
        // given
        SeatStatus seatStatus = SeatStatus.AVAILABLE;

        // when
        boolean isValid = concertValidator.isOccupied(seatStatus);

        // then
        assertFalse(isValid);
    }

    @Test
    void isOccupied_is_null() {
        // given
        SeatStatus seatStatus = null;

        // when
        boolean isValid = concertValidator.isOccupied(seatStatus);

        // then
        assertFalse(isValid);
    }

    @Test
    void isOccupied() {
        // given
        SeatStatus seatStatus = SeatStatus.WAITING_FOR_RESERVATION;

        // when
        boolean isValid = concertValidator.isOccupied(seatStatus);

        // then
        assertTrue(isValid);
    }

    @Test
    void isTaken_is_null() {
        // given
        SeatStatus seatStatus = null;

        // when
        boolean isValid = concertValidator.isTaken(seatStatus);

        // then
        assertFalse(isValid);
    }

    @Test
    void isTaken_not_taken() {
        // given
        SeatStatus seatStatus = SeatStatus.WAITING_FOR_RESERVATION;

        // when
        boolean isValid = concertValidator.isTaken(seatStatus);

        // then
        assertFalse(isValid);
    }

    @Test
    void isTaken() {
        // given
        SeatStatus seatStatus = SeatStatus.TAKEN;

        // when
        boolean isValid = concertValidator.isTaken(seatStatus);

        // then
        assertTrue(isValid);
    }

    @Test
    void checkIfConcertToBeReserved_concertId_is_wrong() {
        ConcertModel concertModel = ConcertModel.create(-1L, "항해코치님 팬미팅", "허재", ConcertStatus.AVAILABLE);

        // when - then
        assertThrows(CustomException.class, () -> concertValidator.checkIfConcertToBeReserved(concertModel));
    }

    @Test
    void checkIfConcertToBeReserved_concertName_is_wrong() {
        // given
        ConcertModel concertModel = ConcertModel.create(1L, "", "허재", ConcertStatus.AVAILABLE);

        // when - then
        assertThrows(CustomException.class, () -> concertValidator.checkIfConcertToBeReserved(concertModel));
    }

    @Test
    void checkIfConcertToBeReserved_concertStatus_is_wrong() {
        // given
        ConcertModel concertModel = ConcertModel.create(1L, "허재코치님 팬미팅", "허재", ConcertStatus.CLOSED);

        // when - then
        assertThrows(CustomException.class, () -> concertValidator.checkIfConcertToBeReserved(concertModel));
    }

    @Test
    void checkIfConcertToBeReserved() {
        // given
        ConcertModel concertModel = ConcertModel.create(1L, "허재코치님 팬미팅", "허재", ConcertStatus.AVAILABLE);

        // when
        concertValidator.checkIfConcertToBeReserved(concertModel);

        // then
        assertTrue(true);
    }

    @Test
    void checkIfPerformanceToBeReserved() {
        // given
        PerformanceModel performanceModel = PerformanceModel.create(
                1L,
                BigDecimal.valueOf(30000),
                50,
                DateUtils.createTemporalDateByIntParameters(2024,11, 20, 14 ,0,0));
        Date currentDate = DateUtils.getSysDate();

        // when
        concertValidator.checkIfPerformanceToBeReserved(performanceModel, currentDate);

        // then
        assertTrue(true);
    }

    @Test
    void checkIfPerformanceToBeReserved_performanceId_is_wrong() {
        // given
        PerformanceModel performanceModel = PerformanceModel.create(
                -1L,
                BigDecimal.valueOf(30000),
                50,
                DateUtils.createTemporalDateByIntParameters(2024,11, 20, 14 ,0,0));
        Date currentDate = DateUtils.getSysDate();

        // when - then
        assertThrows(CustomException.class, () -> concertValidator.checkIfPerformanceToBeReserved(performanceModel, currentDate));
    }

    @Test
    void checkIfPerformanceToBeReserved_performedAt_is_wrong() {
        // given
        PerformanceModel performanceModel = PerformanceModel.create(
                1L,
                BigDecimal.valueOf(30000),
                -50,
                DateUtils.createTemporalDateByIntParameters(2024,11, 20, 14 ,0,0));

        Date currentDate = DateUtils.getSysDate();

        // when - then
        assertThrows(CustomException.class, () -> concertValidator.checkIfPerformanceToBeReserved(performanceModel, currentDate));
    }

    @Test
    void checkIfPerformanceToBeReserved_capacity_is_wrong() {
        // given
        PerformanceModel performanceModel = PerformanceModel.create(
                1L,
                BigDecimal.valueOf(30000),
                50,
                DateUtils.createTemporalDateByIntParameters(2023,11, 20, 14 ,0,0));

        Date currentDate = DateUtils.getSysDate();

        // when - then
        assertThrows(CustomException.class, () -> concertValidator.checkIfPerformanceToBeReserved(performanceModel, currentDate));
    }

    @Test
    void checkIfSeatToBeReserved_seatId_is_wrong() {
        // given
        SeatModel seatModel = SeatModel.create(-1L, 1L, 1L, "50", SeatStatus.AVAILABLE);

        // when - then
        assertThrows(CustomException.class, () -> concertValidator.checkIfSeatToBeReserved(seatModel));
    }

    @Test
    void checkIfSeatToBeReserved_performanceId_is_wrong() {
        // given
        SeatModel seatModel = SeatModel.create(1L, -1L, 1L, "50", SeatStatus.AVAILABLE);

        // when - then
        assertThrows(CustomException.class, () -> concertValidator.checkIfSeatToBeReserved(seatModel));
    }

    @Test
    void checkIfSeatToBeReserved_concertId_is_wrong() {
        // given
        SeatModel seatModel = SeatModel.create(1L, 1L, -1L, "50", SeatStatus.AVAILABLE);

        // when - then
        assertThrows(CustomException.class, () -> concertValidator.checkIfSeatToBeReserved(seatModel));
    }

    @Test
    void checkIfSeatToBeReserved_seatNo_is_wrong() {
        // given
        SeatModel seatModel = SeatModel.create(1L, 1L, 1L, "", SeatStatus.AVAILABLE);

        // when - then
        assertThrows(CustomException.class, () -> concertValidator.checkIfSeatToBeReserved(seatModel));
    }

    @Test
    void checkIfSeatToBeReserved_seatStatus_is_wrong() {
        // given
        SeatModel seatModel = SeatModel.create(1L, 1L, 1L, "50", SeatStatus.TAKEN);

        // when - then
        assertThrows(CustomException.class, () -> concertValidator.checkIfSeatToBeReserved(seatModel));
    }

    @Test
    void checkIfSeatToBeReserved() {
        // given
        SeatModel seatModel = SeatModel.create(1L, 1L, 1L, "50", SeatStatus.AVAILABLE);

        // when
        concertValidator.checkIfSeatToBeReserved(seatModel);

        // then
        assertTrue(true);
    }

    @Test
    void checkIfSeatNotAssigned_already_occupied() {
        // given
        SeatModel asisSeatModel = SeatModel.create(1L, 1L, 1L, "50", SeatStatus.WAITING_FOR_RESERVATION);
        given(seatRepository.findById(anyLong())).willReturn(Optional.of(asisSeatModel));

        // when - then
        assertThrows(CustomException.class, () -> concertValidator.checkIfSeatNotAssigned(1L));
    }

    @Test
    void checkIfSeatNotAssigned_already_taken() {
        // given
        SeatModel asisSeatModel = SeatModel.create(1L, 1L, 1L, "50", SeatStatus.TAKEN);
        given(seatRepository.findById(anyLong())).willReturn(Optional.of(asisSeatModel));

        // when - then
        assertThrows(CustomException.class, () -> concertValidator.checkIfSeatNotAssigned(1L));
    }

    @Test
    void checkIfSeatNotAssigned_not_available() {
        // given
        SeatModel asisSeatModel = SeatModel.create(1L, 1L, 1L, "50", SeatStatus.NOT_AVAILABLE);
        given(seatRepository.findById(anyLong())).willReturn(Optional.of(asisSeatModel));

        // when - then
        assertThrows(CustomException.class, () -> concertValidator.checkIfSeatNotAssigned(1L));
    }
}