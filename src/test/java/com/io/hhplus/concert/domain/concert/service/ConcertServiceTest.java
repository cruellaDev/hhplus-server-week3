package com.io.hhplus.concert.domain.concert.service;

import com.io.hhplus.concert.common.enums.ConcertStatus;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.common.utils.DateUtils;
import com.io.hhplus.concert.domain.concert.ConcertCommand;
import com.io.hhplus.concert.domain.concert.ConcertService;
import com.io.hhplus.concert.domain.concert.dto.AvailableSeatInfo;
import com.io.hhplus.concert.domain.concert.dto.ReservationInfo;
import com.io.hhplus.concert.domain.concert.model.*;
import com.io.hhplus.concert.domain.concert.ConcertRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Disabled
class ConcertServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @InjectMocks
    private ConcertService concertService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 콘서트_등록_시_콘서트_명이_blank_이면_예외를_반환한다() {
        // given
        ConcertCommand.RegisterConcertCommand command = ConcertCommand.RegisterConcertCommand.builder()
                .concertName("  ")
                .build();

        // when - then
        assertThatThrownBy(() -> concertService.registerConcert(command))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.CONCERT_INVALID);
        assertThatThrownBy(() -> concertService.registerConcert(command))
                .isInstanceOf(CustomException.class)
                .extracting("messageDetail")
                .isEqualTo("콘서트 명이 존재하지 않습니다.");
    }

    @Test
    void 콘서트_등록_시_아티스트_명이_blank_이면_예외를_반환한다() {
        // given
        ConcertCommand.RegisterConcertCommand command = ConcertCommand.RegisterConcertCommand.builder()
                .concertName("항플 콘서트")
                .artistName("    ")
                .build();

        // when - then
        assertThatThrownBy(() -> concertService.registerConcert(command))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.CONCERT_INVALID);
        assertThatThrownBy(() -> concertService.registerConcert(command))
                .isInstanceOf(CustomException.class)
                .extracting("messageDetail")
                .isEqualTo("아티스트 명이 존재하지 않습니다.");
    }

    @Test
    void 콘서트_등록_시_예매_시작_일시가_없으면_예외를_반환한다() {
        // given
        ConcertCommand.RegisterConcertCommand command = ConcertCommand.RegisterConcertCommand.builder()
                .concertName("항플 콘서트")
                .artistName("항해킴")
                .build();

        // when - then
        assertThatThrownBy(() -> concertService.registerConcert(command))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.CONCERT_INVALID);
        assertThatThrownBy(() -> concertService.registerConcert(command))
                .isInstanceOf(CustomException.class)
                .extracting("messageDetail")
                .isEqualTo("예매 기간 정보가 존재하지 않습니다.");
    }

    @Test
    void 콘서트_등록_시_예매_종료_일시가_현재_일시보다_이후이면_예외를_반환한다() {
        // given
        ConcertCommand.RegisterConcertCommand command = ConcertCommand.RegisterConcertCommand.builder()
                .concertName("항플 콘서트")
                .artistName("항해킴")
                .bookBeginAt(DateUtils.getSysDate())
                .bookEndAt(DateUtils.createTemporalDateByIntParameters(2024,8,1,23,59,59))
                .build();

        // when - then
        assertThatThrownBy(() -> concertService.registerConcert(command))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.CONCERT_INVALID);
        assertThatThrownBy(() -> concertService.registerConcert(command))
                .isInstanceOf(CustomException.class)
                .extracting("messageDetail")
                .isEqualTo("예매 종료 일시는 현재 일시보다 이후여야 합니다.");
    }

    @Test
    void 콘서트_등록_시_예매_시작_일시가_예매_종료_일시보다_이후이면_예외를_반환한다() {
        // given
        ConcertCommand.RegisterConcertCommand command = ConcertCommand.RegisterConcertCommand.builder()
                .concertName("항플 콘서트")
                .artistName("항해킴")
                .bookBeginAt(DateUtils.createTemporalDateByIntParameters(2024,9,2,23,59,59))
                .bookEndAt(DateUtils.createTemporalDateByIntParameters(2024,9,1,23,59,59))
                .build();

        // when - then
        assertThatThrownBy(() -> concertService.registerConcert(command))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.CONCERT_INVALID);
        assertThatThrownBy(() -> concertService.registerConcert(command))
                .isInstanceOf(CustomException.class)
                .extracting("messageDetail")
                .isEqualTo("예매 시작 일시는 예매 종료 일시보다 이전이어야 합니다.");
    }

    /**
     * 예약 가능 콘서트 목록 조회
     */
    @Test
    void 예약_콘서트_목록이_존재하지_않으면_빈배열을_반환한다() {
        // given
        List<Concert> concerts = List.of();
        given(concertRepository.findConcerts()).willReturn(concerts);

        // when
        List<Concert> result = concertService.getConcerts();

        // then
        assertThat(result).hasSize(0);
    }

    /**
     * 예약 가능 콘서트 목록 조회
     */
    @Test
    void 예약_콘서트_목록을_조회한다() {
        // given
        List<Concert> concerts = List.of(
                Concert.builder().build()
        );
        given(concertRepository.findConcerts()).willReturn(concerts);

        // when
        List<Concert> result = concertService.getConcerts();

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    void 콘서트_일정_등록_시_공연_일자가_현재_일시보다_이전이면_예외를_반환한다() {
        // given
        ConcertCommand.RegisterConcertScheduleCommand command = ConcertCommand.RegisterConcertScheduleCommand.builder()
                .concertId(1L)
                .performedAt(DateUtils.createTemporalDateByIntParameters(2022,1,1,1,0,0))
                .build();

        // when - then
        assertThatThrownBy(() -> concertService.registerConcertSchedule(command))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.CONCERT_SCHEDULE_INVALID);
        assertThatThrownBy(() -> concertService.registerConcertSchedule(command))
                .isInstanceOf(CustomException.class)
                .extracting("messageDetail")
                .isEqualTo("예매 종료 일시는 현재 일시보다 이후여야 합니다.");
    }

    /**
     * 현재일시 기준 특정 콘서트의 예약 가능 일정 목록을 조회한댜.
     */
    @Test
    void 현재_일시_기준_특정_콘서트의_예약_가능_일정_목록을_조회한다() {
        // given
        List<ConcertSchedule> concertSchedules = List.of(
                ConcertSchedule.builder()
                .concertId(1L)
                .concertScheduleId(1L)
                .performedAt(DateUtils.createTemporalDateByIntParameters(2024,12,9,23,0,0))
                .build());
        given(concertRepository.findConcertSchedules(anyLong())).willReturn(concertSchedules);

        // when
        List<ConcertSchedule> result = concertService.getAvailableSchedules(1L);

        // then
        assertThat(result).hasSize(1);
    }

    /**
     * 현재일시 기준 특정 콘서트의 예약 가능 일정 목록을 조회한댜.
     */
    @Test
    void 현재_일시_기준_특정_콘서트의_예약_가능_일정_목록이_없으면_빈배열을_반환한다() {
        // given
        List<ConcertSchedule> concertSchedules = List.of();
        given(concertRepository.findConcertSchedules(anyLong())).willReturn(concertSchedules);

        // when
        List<ConcertSchedule> result = concertService.getAvailableSchedules(999L);

        // then
        assertThat(result).hasSize(0);
    }

    /**
     * 현재일시 기준 특정 콘서트의 예약 가능 공연 구역 좌석 목록을 조회한댜.
     */
    @Test
    void 현재_일시_기준_특정_콘서트의_공연_좌석_목록_조회_시_콘서트가_없으면_예외로_처리한다() {
        // given
        given(concertRepository.findConcertSeat(anyLong(), anyLong())).willReturn(Optional.empty());

        // when - then
        assertThatThrownBy(() -> concertService.getAvailableSeats(999L, 999L))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.CONCERT_SEAT_NOT_FOUND);

    }

    /**
     * 현재일시 기준 특정 콘서트의 예약 가능 공연 구역 좌석 목록을 조회한댜.
     */
    @Test
    void 현재_일시_기준_특정_콘서트의_공연_좌석_목록_조회_시_일정이_없으면_예외로_처리한다() {
        // given
        given(concertRepository.findConcertSeat(anyLong(), anyLong())).willReturn(Optional.empty());

        // when - then
        assertThatThrownBy(() -> concertService.getAvailableSeats(1L, 999L))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.CONCERT_SEAT_NOT_FOUND);

    }

    /**
     * 현재일시 기준 특정 콘서트의 예약 가능 공연 좌석 목록을 조회한댜.
     */
    @Test
    void 현재_일시_기준_특정_콘서트의_공연_구역의_좌석_인원이_0이면_빈_배열을_반환한다() {
        // given
        ConcertSeat seat = ConcertSeat.builder()
                .concertSeatId(1L)
                .concertId(1L)
                .concertScheduleId(1L)
                .seatPrice(BigDecimal.valueOf(10000))
                .seatCapacity(0L)
                .build();
        given(concertRepository.findConcertSeat(anyLong(), anyLong())).willReturn(Optional.of(seat));
        given(concertRepository.findNotOccupiedSeatFromTicket(anyLong(), anyLong(), anyString())).willReturn(Optional.empty());

        // when
        List<AvailableSeatInfo> result = concertService.getAvailableSeats(1L, 1L);

        // then
        assertThat(result).isEmpty();
    }

    /**
     * 현재일시 기준 특정 콘서트의 예약 가능 공연 좌석 목록을 조회한댜.
     */
    @Test
    void 현재_일시_기준_특정_콘서트의_공연_좌석_목록을_조회한다_모두_예약_가능() {
        // given
        ConcertSeat seat = ConcertSeat.builder()
                .concertSeatId(1L)
                .concertId(1L)
                .concertScheduleId(1L)
                .seatPrice(BigDecimal.valueOf(10000))
                .seatCapacity(3L)
                .build();
        Ticket seat1 = Ticket.builder()
                .concertId(1L)
                .concertScheduleId(1L)
                .seatNumber("1")
                .ticketPrice(BigDecimal.valueOf(10000))
                .build();
        Ticket seat2 = Ticket.builder()
                .concertId(1L)
                .concertScheduleId(1L)
                .seatNumber("2")
                .ticketPrice(BigDecimal.valueOf(10000))
                .build();
        Ticket seat3 = Ticket.builder()
                .concertId(1L)
                .concertScheduleId(1L)
                .seatNumber("3")
                .ticketPrice(BigDecimal.valueOf(10000))
                .build();

        given(concertRepository.findConcertSeat(anyLong(), anyLong())).willReturn(Optional.of(seat));
        given(concertRepository.findNotOccupiedSeatFromTicket(anyLong(), anyLong(), anyString())).willReturn(Optional.of(seat1));
        given(concertRepository.findNotOccupiedSeatFromTicket(anyLong(), anyLong(), anyString())).willReturn(Optional.of(seat2));
        given(concertRepository.findNotOccupiedSeatFromTicket(anyLong(), anyLong(), anyString())).willReturn(Optional.of(seat3));

        // when
        List<AvailableSeatInfo> result = concertService.getAvailableSeats(1L, 1L);

        // then
        assertThat(result).hasSize(3);
    }

    /**
     * 현재일시 기준 특정 콘서트의 예약 가능 공연 좌석 목록을 조회한댜.
     */
    @Test
    void 현재_일시_기준_특정_콘서트의_공연_좌석_목록을_조회한다_모두_예약된_상태() {
        // given
        ConcertSeat seat = ConcertSeat.builder()
                .concertSeatId(1L)
                .concertId(1L)
                .concertScheduleId(1L)
                .seatPrice(BigDecimal.valueOf(10000))
                .seatCapacity(3L)
                .build();

        given(concertRepository.findConcertSeat(anyLong(), anyLong())).willReturn(Optional.of(seat));
        given(concertRepository.findNotOccupiedSeatFromTicket(anyLong(), anyLong(), anyString())).willReturn(Optional.empty());
        given(concertRepository.findNotOccupiedSeatFromTicket(anyLong(), anyLong(), anyString())).willReturn(Optional.empty());
        given(concertRepository.findNotOccupiedSeatFromTicket(anyLong(), anyLong(), anyString())).willReturn(Optional.empty());

        // when
        List<AvailableSeatInfo> result = concertService.getAvailableSeats(1L, 1L);

        // then
        assertThat(result).hasSize(0);
    }

    /**
     * 빈 좌석에 대해 예약을 요청한다.
     */
    @Test
    void 예약_요청_이미_예전_예약이_존재하는_경우_예외를_반환한다() {
        // given
        Reservation reservation = Reservation.builder()
                .reservationId(1L)
                .customerId(1L)
                .bookerName("박항해")
                .createdAt(DateUtils.createTemporalDateByIntParameters(2024, 8,5,11,56,30))
                .build();
        given(concertRepository.findReservationAlreadyExists(anyLong(), anyLong(), anyLong(), any())).willReturn(Optional.of(reservation));

        // when - then
        ConcertCommand.ReserveSeatsCommand command = ConcertCommand.ReserveSeatsCommand.builder()
                .concertId(1L)
                .concertScheduleId(1L)
                .concertSeatId(1L)
                .seatNumbers(List.of("1"))
                .customerId(1L)
                .bookerName("박항해")
                .build();
        assertThatThrownBy(() -> concertService.reserveSeats(command))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.ALREADY_RESERVED);
    }

    /**
     * 빈 좌석에 대해 예약을 요청한다.
     */
    @Test
    void 예약_요청_시_예약할_콘서트가_없을_경우_예외를_반환한다() {
        // given
        given(concertRepository.findReservationAlreadyExists(anyLong(), anyLong(), anyLong(), any())).willReturn(Optional.empty());
        given(concertRepository.findConcert(anyLong())).willReturn(Optional.empty());

        // when - then
        ConcertCommand.ReserveSeatsCommand command = ConcertCommand.ReserveSeatsCommand.builder()
                .concertId(1L)
                .concertScheduleId(1L)
                .concertSeatId(1L)
                .seatNumbers(List.of("1"))
                .customerId(1L)
                .bookerName("박항해")
                .build();
        assertThatThrownBy(() -> concertService.reserveSeats(command))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.CONCERT_NOT_FOUND);
    }

    /**
     * 빈 좌석에 대해 예약을 요청한다.
     */
    @Test
    void 예약_요청_시_예약할_콘서트_일정이_없을_경우_예외를_반환한다() {
        // given
        Concert concert = Concert.builder()
                .concertId(1L)
                .concertName("항해콘서트")
                .artistName("김항해")
                .concertStatus(ConcertStatus.AVAILABLE)
                .bookBeginAt(DateUtils.createTemporalDateByIntParameters(2024,7,1,0,0,0))
                .bookEndAt(DateUtils.createTemporalDateByIntParameters(2024,12,31,23,59,59))
                .build();
        given(concertRepository.findReservationAlreadyExists(anyLong(), anyLong(), anyLong(), any())).willReturn(Optional.empty());
        given(concertRepository.findConcert(anyLong())).willReturn(Optional.of(concert));

        // when - then
        ConcertCommand.ReserveSeatsCommand command = ConcertCommand.ReserveSeatsCommand.builder()
                .concertId(1L)
                .concertScheduleId(1L)
                .concertSeatId(1L)
                .seatNumbers(List.of("1"))
                .customerId(1L)
                .bookerName("박항해")
                .build();
        assertThatThrownBy(() -> concertService.reserveSeats(command))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.CONCERT_SCHEDULE_NOT_FOUND);
    }

    /**
     * 빈 좌석에 대해 예약을 요청한다.
     */
    @Test
    void 예약_요청_시_예약할_콘서트_좌석_정보가_없을_경우_예외를_반환한다() {
        // given
        Concert concert = Concert.builder()
                .concertId(1L)
                .concertName("항해콘서트")
                .artistName("김항해")
                .concertStatus(ConcertStatus.AVAILABLE)
                .bookBeginAt(DateUtils.createTemporalDateByIntParameters(2024,7,1,0,0,0))
                .bookEndAt(DateUtils.createTemporalDateByIntParameters(2024,12,31,23,59,59))
                .build();
        ConcertSchedule concertSchedule = ConcertSchedule.builder()
                .concertId(1L)
                .concertScheduleId(1L)
                .performedAt(DateUtils.createTemporalDateByIntParameters(2024,11,3,17,0,0))
                .build();
        given(concertRepository.findReservationAlreadyExists(anyLong(), anyLong(), anyLong(), any())).willReturn(Optional.empty());
        given(concertRepository.findConcert(anyLong())).willReturn(Optional.of(concert));
        given(concertRepository.findConcertSchedule(anyLong(), anyLong())).willReturn(Optional.of(concertSchedule));

        // when - then
        ConcertCommand.ReserveSeatsCommand command = ConcertCommand.ReserveSeatsCommand.builder()
                .concertId(1L)
                .concertScheduleId(1L)
                .concertSeatId(1L)
                .seatNumbers(List.of("1"))
                .customerId(1L)
                .bookerName("박항해")
                .build();
        assertThatThrownBy(() -> concertService.reserveSeats(command))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.CONCERT_SEAT_NOT_FOUND);
    }

    /**
     * 빈 좌석에 대해 예약을 요청한다.
     */
    @Test
    void 예약_요청_시_콘서트_예매기간이_아닌_경우_예외를_반환한다() {
        // given
        Concert concert = Concert.builder()
                .concertId(1L)
                .concertName("항해콘서트")
                .artistName("김항해")
                .concertStatus(ConcertStatus.AVAILABLE)
                .bookBeginAt(DateUtils.createTemporalDateByIntParameters(2024,11,1,0,0,0))
                .bookEndAt(DateUtils.createTemporalDateByIntParameters(2024,12,31,23,59,59))
                .build();
        ConcertSchedule concertSchedule = ConcertSchedule.builder()
                .concertId(1L)
                .concertScheduleId(1L)
                .performedAt(DateUtils.createTemporalDateByIntParameters(2024,11,3,17,0,0))
                .build();
        ConcertSeat concertSeat = ConcertSeat.builder()
                .concertId(1L)
                .concertScheduleId(1L)
                .concertSeatId(1L)
                .seatPrice(BigDecimal.valueOf(10000))
                .seatCapacity(3L)
                .build();
        Reservation reservation = Reservation.builder()
                .reservationId(1L)
                .customerId(1L)
                .bookerName("박항해")
                .createdAt(DateUtils.getSysDate())
                .build();
        Ticket ticket = Ticket.builder()
                .reservationId(1L)
                .concertId(1L)
                .concertScheduleId(1L)
                .seatNumber("1")
                .ticketPrice(BigDecimal.valueOf(10000))
                .createdAt(DateUtils.getSysDate())
                .build();
        given(concertRepository.findReservationAlreadyExists(anyLong(), anyLong(), anyLong(), any())).willReturn(Optional.empty());
        given(concertRepository.findConcert(anyLong())).willReturn(Optional.of(concert));
        given(concertRepository.findConcertSchedule(anyLong(), anyLong())).willReturn(Optional.of(concertSchedule));
        given(concertRepository.findConcertSeat(anyLong(), anyLong())).willReturn(Optional.of(concertSeat));
        given(concertRepository.findNotOccupiedSeatFromTicket(anyLong(), anyLong(), anyString())).willReturn(Optional.empty());
        given(concertRepository.saveReservation(any(Reservation.class))).willReturn(reservation);
        given(concertRepository.saveTicket(any(Ticket.class))).willReturn(ticket);

        // when - then
        ConcertCommand.ReserveSeatsCommand command = ConcertCommand.ReserveSeatsCommand.builder()
                .concertId(1L)
                .concertScheduleId(1L)
                .concertSeatId(1L)
                .seatNumbers(List.of("1"))
                .customerId(1L)
                .bookerName("박항해")
                .build();

        assertThatThrownBy(() -> concertService.reserveSeats(command))
                .isInstanceOf(CustomException.class);
    }

    /**
     * 빈 좌석에 대해 예약을 요청한다.
     */
    @Test
    void 예약_요청_시_콘서트_공연기간이_지난_경우_예외를_반환한다() {
        // given
        Concert concert = Concert.builder()
                .concertId(1L)
                .concertName("항해콘서트")
                .artistName("김항해")
                .concertStatus(ConcertStatus.AVAILABLE)
                .bookBeginAt(DateUtils.createTemporalDateByIntParameters(2024,8,1,0,0,0))
                .bookEndAt(DateUtils.createTemporalDateByIntParameters(2024,12,31,23,59,59))
                .build();
        ConcertSchedule concertSchedule = ConcertSchedule.builder()
                .concertId(1L)
                .concertScheduleId(1L)
                .performedAt(DateUtils.createTemporalDateByIntParameters(2024,8,3,17,0,0))
                .build();
        ConcertSeat concertSeat = ConcertSeat.builder()
                .concertId(1L)
                .concertScheduleId(1L)
                .concertSeatId(1L)
                .seatPrice(BigDecimal.valueOf(10000))
                .seatCapacity(3L)
                .build();
        Reservation reservation = Reservation.builder()
                .reservationId(1L)
                .customerId(1L)
                .bookerName("박항해")
                .createdAt(DateUtils.getSysDate())
                .build();
        Ticket ticket = Ticket.builder()
                .reservationId(1L)
                .concertId(1L)
                .concertScheduleId(1L)
                .seatNumber("1")
                .ticketPrice(BigDecimal.valueOf(10000))
                .createdAt(DateUtils.getSysDate())
                .build();
        given(concertRepository.findReservationAlreadyExists(anyLong(), anyLong(), anyLong(), any())).willReturn(Optional.empty());
        given(concertRepository.findConcert(anyLong())).willReturn(Optional.of(concert));
        given(concertRepository.findConcertSchedule(anyLong(), anyLong())).willReturn(Optional.of(concertSchedule));
        given(concertRepository.findConcertSeat(anyLong(), anyLong())).willReturn(Optional.of(concertSeat));
        given(concertRepository.findNotOccupiedSeatFromTicket(anyLong(), anyLong(), anyString())).willReturn(Optional.empty());
        given(concertRepository.saveReservation(any(Reservation.class))).willReturn(reservation);
        given(concertRepository.saveTicket(any(Ticket.class))).willReturn(ticket);

        // when - then
        ConcertCommand.ReserveSeatsCommand command = ConcertCommand.ReserveSeatsCommand.builder()
                .concertId(1L)
                .concertScheduleId(1L)
                .concertSeatId(1L)
                .seatNumbers(List.of("1"))
                .customerId(1L)
                .bookerName("박항해")
                .build();

        assertThatThrownBy(() -> concertService.reserveSeats(command))
                .isInstanceOf(CustomException.class);
    }

    /**
     * 빈 좌석에 대해 예약을 요청한다.
     */
    @Test
    void 예약_요청_시_콘서트_잔여_좌석이_0일_경우_예외를_반환한다() {
        // given
        Concert concert = Concert.builder()
                .concertId(1L)
                .concertName("항해콘서트")
                .artistName("김항해")
                .concertStatus(ConcertStatus.AVAILABLE)
                .bookBeginAt(DateUtils.createTemporalDateByIntParameters(2024,8,1,0,0,0))
                .bookEndAt(DateUtils.createTemporalDateByIntParameters(2024,12,31,23,59,59))
                .build();
        ConcertSchedule concertSchedule = ConcertSchedule.builder()
                .concertId(1L)
                .concertScheduleId(1L)
                .performedAt(DateUtils.createTemporalDateByIntParameters(2024,12,3,17,0,0))
                .build();
        ConcertSeat concertSeat = ConcertSeat.builder()
                .concertId(1L)
                .concertScheduleId(1L)
                .concertSeatId(1L)
                .seatPrice(BigDecimal.valueOf(10000))
                .seatCapacity(0L)
                .build();
        Reservation reservation = Reservation.builder()
                .reservationId(1L)
                .customerId(1L)
                .bookerName("박항해")
                .createdAt(DateUtils.getSysDate())
                .build();
        Ticket ticket = Ticket.builder()
                .reservationId(1L)
                .concertId(1L)
                .concertScheduleId(1L)
                .seatNumber("1")
                .ticketPrice(BigDecimal.valueOf(10000))
                .createdAt(DateUtils.getSysDate())
                .build();
        given(concertRepository.findReservationAlreadyExists(anyLong(), anyLong(), anyLong(), any())).willReturn(Optional.empty());
        given(concertRepository.findConcert(anyLong())).willReturn(Optional.of(concert));
        given(concertRepository.findConcertSchedule(anyLong(), anyLong())).willReturn(Optional.of(concertSchedule));
        given(concertRepository.findConcertSeat(anyLong(), anyLong())).willReturn(Optional.of(concertSeat));
        given(concertRepository.findNotOccupiedSeatFromTicket(anyLong(), anyLong(), anyString())).willReturn(Optional.empty());
        given(concertRepository.saveReservation(any(Reservation.class))).willReturn(reservation);
        given(concertRepository.saveTicket(any(Ticket.class))).willReturn(ticket);

        // when - then
        ConcertCommand.ReserveSeatsCommand command = ConcertCommand.ReserveSeatsCommand.builder()
                .concertId(1L)
                .concertScheduleId(1L)
                .concertSeatId(1L)
                .seatNumbers(List.of("1"))
                .customerId(1L)
                .bookerName("박항해")
                .build();

        assertThatThrownBy(() -> concertService.reserveSeats(command))
                .isInstanceOf(CustomException.class);
    }

    /**
     * 빈 좌석에 대해 예약을 요청한다.
     */
    @Test
    void 예약_요청_시_빈_좌석이_아닐_경우_이선좌_예외를_반환한다() {
        // given
        Concert concert = Concert.builder()
                .concertId(1L)
                .concertName("항해콘서트")
                .artistName("김항해")
                .concertStatus(ConcertStatus.AVAILABLE)
                .bookBeginAt(DateUtils.createTemporalDateByIntParameters(2024,7,1,0,0,0))
                .bookEndAt(DateUtils.createTemporalDateByIntParameters(2024,12,31,23,59,59))
                .build();
        ConcertSchedule concertSchedule = ConcertSchedule.builder()
                .concertId(1L)
                .concertScheduleId(1L)
                .performedAt(DateUtils.createTemporalDateByIntParameters(2024,11,3,17,0,0))
                .build();
        ConcertSeat concertSeat = ConcertSeat.builder()
                .concertId(1L)
                .concertScheduleId(1L)
                .concertSeatId(1L)
                .seatPrice(BigDecimal.valueOf(10000))
                .seatCapacity(3L)
                .build();
        Ticket seat = Ticket.builder()
                .concertId(1L)
                .concertScheduleId(1L)
                .seatNumber("1")
                .ticketPrice(BigDecimal.valueOf(10000))
                .build();
        given(concertRepository.findReservationAlreadyExists(anyLong(), anyLong(), anyLong(), any())).willReturn(Optional.empty());
        given(concertRepository.findConcert(anyLong())).willReturn(Optional.of(concert));
        given(concertRepository.findConcertSchedule(anyLong(), anyLong())).willReturn(Optional.of(concertSchedule));
        given(concertRepository.findConcertSeat(anyLong(), anyLong())).willReturn(Optional.of(concertSeat));
        given(concertRepository.findNotOccupiedSeatFromTicket(anyLong(), anyLong(), anyString())).willReturn(Optional.of(seat));

        // when - then
        ConcertCommand.ReserveSeatsCommand command = ConcertCommand.ReserveSeatsCommand.builder()
                .concertId(1L)
                .concertScheduleId(1L)
                .concertSeatId(1L)
                .seatNumbers(List.of("1"))
                .customerId(1L)
                .bookerName("박항해")
                .build();
        assertThatThrownBy(() -> concertService.reserveSeats(command))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.SEAT_TAKEN);
    }

    /**
     * 빈 좌석에 대해 예약을 요청한다.
     */
    @Test
    void 예약_요청_시_빈_좌석일_경우_성공한다() {
        // given
        Concert concert = Concert.builder()
                .concertId(1L)
                .concertName("항해콘서트")
                .artistName("김항해")
                .concertStatus(ConcertStatus.AVAILABLE)
                .bookBeginAt(DateUtils.createTemporalDateByIntParameters(2024,7,1,0,0,0))
                .bookEndAt(DateUtils.createTemporalDateByIntParameters(2024,12,31,23,59,59))
                .build();
        ConcertSchedule concertSchedule = ConcertSchedule.builder()
                .concertId(1L)
                .concertScheduleId(1L)
                .performedAt(DateUtils.createTemporalDateByIntParameters(2024,11,3,17,0,0))
                .build();
        ConcertSeat concertSeat = ConcertSeat.builder()
                .concertId(1L)
                .concertScheduleId(1L)
                .concertSeatId(1L)
                .seatPrice(BigDecimal.valueOf(10000))
                .seatCapacity(3L)
                .build();
        Reservation reservation = Reservation.builder()
                .reservationId(1L)
                .customerId(1L)
                .bookerName("박항해")
                .createdAt(DateUtils.getSysDate())
                .build();
        Ticket ticket = Ticket.builder()
                .reservationId(1L)
                .concertId(1L)
                .concertScheduleId(1L)
                .seatNumber("1")
                .ticketPrice(BigDecimal.valueOf(10000))
                .createdAt(DateUtils.getSysDate())
                .build();
        given(concertRepository.findReservationAlreadyExists(anyLong(), anyLong(), anyLong(), any())).willReturn(Optional.empty());
        given(concertRepository.findConcert(anyLong())).willReturn(Optional.of(concert));
        given(concertRepository.findConcertSchedule(anyLong(), anyLong())).willReturn(Optional.of(concertSchedule));
        given(concertRepository.findConcertSeat(anyLong(), anyLong())).willReturn(Optional.of(concertSeat));
        given(concertRepository.findNotOccupiedSeatFromTicket(anyLong(), anyLong(), anyString())).willReturn(Optional.empty());
        given(concertRepository.saveReservation(any(Reservation.class))).willReturn(reservation);
        given(concertRepository.saveTicket(any(Ticket.class))).willReturn(ticket);

        // when - then
        ConcertCommand.ReserveSeatsCommand command = ConcertCommand.ReserveSeatsCommand.builder()
                .concertId(1L)
                .concertScheduleId(1L)
                .concertSeatId(1L)
                .seatNumbers(List.of("1"))
                .customerId(1L)
                .bookerName("박항해")
                .build();
        ReservationInfo reservationInfo = concertService.reserveSeats(command);

        assertAll(() -> assertEquals(reservation.reservationId(), reservationInfo.reservation().reservationId()),
                () -> assertEquals(1, reservationInfo.tickets().size()));
    }

    /**
     * 예약을 완료한다.
     */
    @Test
    void 예약_완료_시_조회되는_예약_데이터가_없는_경우_예외를_반환한다() {
        // given
        given(concertRepository.findReservation(anyLong(), anyLong())).willReturn(Optional.empty());

        // when - then
        ConcertCommand.ConfirmReservationCommand command = ConcertCommand.ConfirmReservationCommand.builder()
                .customerId(1L)
                .reservationId(1L)
                .build();
        assertThatThrownBy(() -> concertService.confirmReservation(command))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.RESERVATION_NOT_FOUND);
    }

    /**
     * 예약을 완료한다.
     */
    @Test
    void 예약_완료_시_조회되는_예약_데이터가_결제_요청_가능_시간이_경과된_경우_예외를_반환한다() {
        // given
        given(concertRepository.findReservation(anyLong(), anyLong())).willReturn(Optional.empty());

        // when - then
        ConcertCommand.ConfirmReservationCommand command = ConcertCommand.ConfirmReservationCommand.builder()
                .customerId(1L)
                .reservationId(1L)
                .build();
        assertThatThrownBy(() -> concertService.confirmReservation(command))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.RESERVATION_NOT_FOUND);
    }

    /**
     * 예약을 완료한다.
     */
    @Test
    void 예약_완료_시_조회되는_티켓_데이터가_없는_경우_예외를_반환한다() {
        // given
        Reservation reservation = Reservation.builder()
                .reservationId(1L)
                .customerId(1L)
                .bookerName("김항해")
                .build();
        given(concertRepository.findReservation(anyLong(), anyLong())).willReturn(Optional.of(reservation));
        given(concertRepository.findTickets(anyLong())).willReturn(List.of());

        // when - then
        ConcertCommand.ConfirmReservationCommand command = ConcertCommand.ConfirmReservationCommand.builder()
                .customerId(1L)
                .reservationId(1L)
                .build();
        assertThatThrownBy(() -> concertService.confirmReservation(command))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.TICKET_NOT_FOUND);
    }

    /**
     * 예약을 완료한다.
     */
    @Test
    void 예약_완료_시_조회되는_티켓_데이터가_있으나_이미_예약된_티켓인_경우_예외를_반환한다() {
        // given
        Reservation reservation = Reservation.builder()
                .reservationId(1L)
                .customerId(1L)
                .bookerName("김항해")
                .build();
        List<Ticket> tickets = List.of(
                Ticket.builder()
                        .ticketId(1L)
                        .reservationId(1L)
                        .concertId(1L)
                        .concertScheduleId(1L)
                        .concertSeatId(1L)
                        .seatNumber("1")
                        .concertName("항해콘서트")
                        .artistName("김뿅각")
                        .reservedAt(DateUtils.createTemporalDateByIntParameters(2024,8,4,23,59,59))
                        .performedAt(DateUtils.createTemporalDateByIntParameters(2024,11,3,17,0,0))
                        .createdAt(DateUtils.createTemporalDateByIntParameters(2024,8,4,23,59,59))
                        .build()
        );
        given(concertRepository.findReservation(anyLong(), anyLong())).willReturn(Optional.of(reservation));
        given(concertRepository.findTickets(anyLong())).willReturn(tickets);

        // when - then
        ConcertCommand.ConfirmReservationCommand command = ConcertCommand.ConfirmReservationCommand.builder()
                .customerId(1L)
                .reservationId(1L)
                .build();
        assertThatThrownBy(() -> concertService.confirmReservation(command))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.INVALID);
        assertThatThrownBy(() -> concertService.confirmReservation(command))
                .isInstanceOf(CustomException.class)
                .extracting("messageDetail")
                .isEqualTo("예약이 가능한 상태가 아닙니다.");
    }

    /**
     * 예약을 완료한다.
     */
    @Test
    void 예약_완료_시_조회되는_티켓이_있으나_결제요청시간이_경과된_경우_예외를_반환한다() {
        // given
        Reservation reservation = Reservation.builder()
                .reservationId(1L)
                .customerId(1L)
                .bookerName("김항해")
                .build();
        List<Ticket> tickets = List.of(
                Ticket.builder()
                        .ticketId(1L)
                        .reservationId(1L)
                        .concertId(1L)
                        .concertScheduleId(1L)
                        .concertSeatId(1L)
                        .seatNumber("1")
                        .concertName("항해콘서트")
                        .artistName("김뿅각")
                        .performedAt(DateUtils.createTemporalDateByIntParameters(2024,11,3,17,0,0))
                        .createdAt(DateUtils.createTemporalDateByIntParameters(2024,8,4,23,59,59))
                        .build()
        );
        given(concertRepository.findReservation(anyLong(), anyLong())).willReturn(Optional.of(reservation));
        given(concertRepository.findTickets(anyLong())).willReturn(tickets);

        // when - then
        ConcertCommand.ConfirmReservationCommand command = ConcertCommand.ConfirmReservationCommand.builder()
                .customerId(1L)
                .reservationId(1L)
                .build();
        assertThatThrownBy(() -> concertService.confirmReservation(command))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.INVALID);
        assertThatThrownBy(() -> concertService.confirmReservation(command))
                .isInstanceOf(CustomException.class)
                .extracting("messageDetail")
                .isEqualTo("예약이 가능한 상태가 아닙니다.");
    }

}