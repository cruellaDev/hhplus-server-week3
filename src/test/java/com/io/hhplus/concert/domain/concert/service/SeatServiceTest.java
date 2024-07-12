package com.io.hhplus.concert.domain.concert.service;

import com.io.hhplus.concert.common.enums.SeatStatus;
import com.io.hhplus.concert.domain.concert.model.Seat;
import com.io.hhplus.concert.domain.concert.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private SeatService seatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 예약 가능 콘서트 공연 좌석 목록 조회 테스트
     */
    @Test
    void getAvailableSeats() {
        // given
        List<Seat> seats = List.of(
                Seat.create(1L, 1L, 1L, "50", SeatStatus.AVAILABLE),
                Seat.create(2L, 1L, 1L, "49", SeatStatus.AVAILABLE)
        );
        given(seatRepository.findAvailableAllByPerformanceId(anyLong())).willReturn(seats);

        // when
        List<Seat> result = seatService.getAvailableSeats(1L);

        // then
        assertThat(result).hasSize(2);
    }

    /**
     * 예약 가능 콘서트 공연 좌석 단건 조회
     */
    @Test
    void getAvailableSeat() {
        // given
        Seat seat = Seat.create(1L, 1L, 1L, "50", SeatStatus.AVAILABLE);
        given(seatRepository.findAvailableOneByPerformanceIdAndSeatId(anyLong(), anyLong())).willReturn(Optional.of(seat));

        // then
        Seat result = seatService.getAvailableSeat(1L, 1L);

        // then
        assertAll(() -> assertEquals(seat.seatId(), result.seatId()),
                () -> assertEquals(seat.performanceId(), result.performanceId()),
                () -> assertEquals(seat.concertId(), result.concertId()),
                () -> assertEquals(seat.seatNo(), result.seatNo()),
                () -> assertEquals(seat.seatStatus(), result.seatStatus()));
    }

    /**
     * 좌석 예약 가능 검증 모두 통과 시
     */
    @Test
    void meetsIfSeatToBeReserved_when_pass_all() {
        // given
        Seat seat = Seat.create(1L, 1L, 1L, "50", SeatStatus.AVAILABLE);

        // when
        boolean isValid = seatService.meetsIfSeatToBeReserved(seat);

        // then
        assertThat(isValid).isTrue();
    }

    /**
     * 좌석 예약 가능 좌석_ID 이상할 시
     */
    @Test
    void meetsIfSeatToBeReserved_when_seatId_is_wrong() {
        // given
        Seat seat = Seat.create(-1L, 1L, 1L, "50", SeatStatus.AVAILABLE);

        // when
        boolean isValid = seatService.meetsIfSeatToBeReserved(seat);

        // then
        assertThat(isValid).isFalse();
    }

    /**
     * 좌석 예약 가능 공연_ID 이상할 시
     */
    @Test
    void meetsIfSeatToBeReserved_when_performanceId_is_wrong() {
        // given
        Seat seat = Seat.create(1L, -1L, 1L, "50", SeatStatus.AVAILABLE);

        // when
        boolean isValid = seatService.meetsIfSeatToBeReserved(seat);

        // then
        assertThat(isValid).isFalse();
    }

    /**
     * 좌석 예약 가능 콘서트_ID 이상할 시
     */
    @Test
    void meetsIfSeatToBeReserved_when_concertId_is_wrong() {
        // given
        Seat seat = Seat.create(1L, 1L, -1L, "50", SeatStatus.AVAILABLE);

        // when
        boolean isValid = seatService.meetsIfSeatToBeReserved(seat);

        // then
        assertThat(isValid).isFalse();
    }

    /**
     * 좌석 예약 가능 좌석번호 이상할 시
     */
    @Test
    void meetsIfSeatToBeReserved_when_seatNo_is_wrong() {
        // given
        Seat seat = Seat.create(1L, 1L, 1L, "", SeatStatus.AVAILABLE);

        // when
        boolean isValid = seatService.meetsIfSeatToBeReserved(seat);

        // then
        assertThat(isValid).isFalse();
    }

    /**
     * 좌석 예약 가능 좌석상태 이상할 시
     */
    @Test
    void meetsIfSeatToBeReserved_when_seatStatus_is_wrong() {
        // given
        Seat seat = Seat.create(1L, 1L, 1L, "50", SeatStatus.TAKEN);

        // when
        boolean isValid = seatService.meetsIfSeatToBeReserved(seat);

        // then
        assertThat(isValid).isFalse();
    }

    /**
     * 좌석 임시 배정 - 빈 좌석일 시
     */
    @Test
    void assignSeatTemporarily() {
        // given
        Seat asisSeat = Seat.create(1L, 1L, 1L, "50", SeatStatus.AVAILABLE);
        Seat tobeSeat = Seat.create(1L, 1L, 1L, "50", SeatStatus.WAITING_FOR_RESERVATION);
        given(seatRepository.findById(anyLong())).willReturn(Optional.of(asisSeat));
        given(seatRepository.save(any(Seat.class))).willReturn(tobeSeat);

        // when
        boolean isAssigned = seatService.assignSeatTemporarily(1L);

        // then
        assertThat(isAssigned).isTrue();
    }

    /**
     * 좌석 임시 배정 - 빈 좌석일 아닐 시
     */
    @Test
    void assignSeatTemporarily_when_already_occupied() {
        // given
        Seat asisSeat = Seat.create(1L, 1L, 1L, "50", SeatStatus.WAITING_FOR_RESERVATION);
        given(seatRepository.findById(anyLong())).willReturn(Optional.of(asisSeat));

        // when
        boolean isAssigned = seatService.assignSeatTemporarily(1L);

        // then
        assertThat(isAssigned).isFalse();
    }

    /**
     * 좌석 임시 배정 - 예약 완료된 좌석 시
     */
    @Test
    void assignSeatTemporarily_when_already_taken() {
        // given
        Seat asisSeat = Seat.create(1L, 1L, 1L, "50", SeatStatus.TAKEN);
        given(seatRepository.findById(anyLong())).willReturn(Optional.of(asisSeat));

        // when
        boolean isAssigned = seatService.assignSeatTemporarily(1L);

        // then
        assertThat(isAssigned).isFalse();
    }

    /**
     * 좌석 임시 배정 - 예약 불가능한 좌석 시
     */
    @Test
    void assignSeatTemporarily_when_not_available() {
        // given
        Seat asisSeat = Seat.create(1L, 1L, 1L, "50", SeatStatus.NOT_AVAILABLE);
        given(seatRepository.findById(anyLong())).willReturn(Optional.of(asisSeat));

        // when
        boolean isAssigned = seatService.assignSeatTemporarily(1L);

        // then
        assertThat(isAssigned).isFalse();
    }

    /**
     * 좌석 임시 배정 취소
     */
    @Test
    void cancelAssignedSeatsByReservationIdAndReservationStatus() {
        // given
        List<Seat> asisSeats = List.of(
                Seat.create(1L, 1L, 1L, "50", SeatStatus.WAITING_FOR_RESERVATION),
                Seat.create(2L, 1L, 1L, "49", SeatStatus.WAITING_FOR_RESERVATION)
        );
        List<Seat> tobeSeats = List.of(
                Seat.create(1L, 1L, 1L, "50", SeatStatus.AVAILABLE),
                Seat.create(2L, 1L, 1L, "49", SeatStatus.AVAILABLE)
        );
        given(seatRepository.findAllByReservationIdAndSeatStatus(anyLong(), any())).willReturn(asisSeats);
        given(seatRepository.save(any(Seat.class))).willReturn(tobeSeats.get(0));
        given(seatRepository.save(any(Seat.class))).willReturn(tobeSeats.get(1));

        // when
        List<Seat> result = seatService.cancelAssignedSeatsByReservationIdAndReservationStatus(1L, SeatStatus.WAITING_FOR_RESERVATION);

        // then
        assertAll(() -> assertEquals(result.get(0).seatStatus(), SeatStatus.AVAILABLE),
                () -> assertEquals(result.get(0).seatStatus(), SeatStatus.AVAILABLE));
    }

}