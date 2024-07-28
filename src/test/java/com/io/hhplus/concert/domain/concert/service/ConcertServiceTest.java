package com.io.hhplus.concert.domain.concert.service;

import com.io.hhplus.concert.application.concert.dto.HeldSeatServiceResponse;
import com.io.hhplus.concert.application.concert.dto.HoldSeatServiceRequest;
import com.io.hhplus.concert.common.enums.ConcertStatus;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.enums.SeatStatus;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.common.utils.DateUtils;
import com.io.hhplus.concert.domain.concert.model.*;
import com.io.hhplus.concert.domain.concert.repository.ConcertRepository;
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
class ConcertServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @InjectMocks
    private ConcertService concertService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

    /**
     * 현재일시 기준 특정 콘서트의 예약 가능 공연 목록을 조회한댜.
     */
    @Test
    void 현재_일시_기준_특정_콘서트의_예약_가능_공연_목록을_조회한다() {
        // given
        List<Performance> performances = List.of(
                Performance.builder()
                .concertId(1L)
                .performanceId(1L)
                .performedAt(DateUtils.createTemporalDateByIntParameters(2024,12,9,23,0,0))
                .build());
        given(concertRepository.findPerformances(anyLong())).willReturn(performances);

        // when
        List<Performance> result = concertService.getAvailablePerformances(1L);

        // then
        assertThat(result).hasSize(1);
    }

    /**
     * 현재일시 기준 특정 콘서트의 예약 가능 공연 목록을 조회한댜.
     */
    @Test
    void 현재_일시_기준_특정_콘서트의_예약_가능_공연_목록이_없으면_빈배열을_반환한다() {
        // given
        List<Performance> performances = List.of();
        given(concertRepository.findPerformances(anyLong())).willReturn(performances);

        // when
        List<Performance> result = concertService.getAvailablePerformances(999L);

        // then
        assertThat(result).hasSize(0);
    }

    /**
     * 현재일시 기준 특정 콘서트의 예약 가능 공연 구역 목록을 조회한다.
     */
    @Test
    void 현재_일시_기준_특정_콘서트의_공연_구역_목록을_조회한다() {
        // given
        List<Area> areas = List.of(
                Area.builder()
                        .areaId(1L)
                        .concertId(1L)
                        .performanceId(1L)
                        .areaName("A")
                        .seatPrice(BigDecimal.valueOf(10000))
                        .seatCapacity(50L)
                        .build()
        );
        given(concertRepository.findAreas(anyLong(), anyLong())).willReturn(areas);

        // when
        List<Area> result = concertService.getAvailableAreas(1L, 1L);

        // then
        assertAll(() -> assertThat(result).hasSize(1),
                () -> assertEquals(1L, result.get(0).concertId()),
                () -> assertEquals(1L, result.get(0).performanceId()),
                () -> assertEquals(1L, result.get(0).areaId()));
    }

    /**
     * 현재일시 기준 특정 콘서트의 예약 가능 공연 구역 목록을 조회한다.
     */
    @Test
    void 현재_일시_기준_특정_콘서트의_공연_구역_목록이_없으면_빈_배열을_반환한다() {
        // given
        List<Area> areas = List.of();
        given(concertRepository.findAreas(anyLong(), anyLong())).willReturn(areas);

        // when
        List<Area> result = concertService.getAvailableAreas(999L, 999L);

        // then
        assertThat(result).isEmpty();
    }

    /**
     * 현재일시 기준 특정 콘서트의 예약 가능 공연 구역 좌석 목록을 조회한댜.
     */
    @Test
    void 현재_일시_기준_특정_콘서트의_공연_구역_좌석_목록_조회_시_구역이_없으면_예외로_처리한다() {
        // given
        given(concertRepository.findArea(anyLong(), anyLong(), anyLong())).willReturn(Optional.empty());

        // when - then
        assertThatThrownBy(() -> concertService.getAvailableSeats(1L, 1L, 1L))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_FOUND);

    }

    /**
     * 현재일시 기준 특정 콘서트의 예약 가능 공연 구역 좌석 목록을 조회한댜.
     */
    @Test
    void 현재_일시_기준_특정_콘서트의_공연_구역의_좌석_인원이_0이면_빈_배열을_반환한다() {
        // given
        Area area = Area.builder()
                .areaId(1L)
                .concertId(1L)
                .performanceId(1L)
                .areaName("A")
                .seatPrice(BigDecimal.valueOf(10000))
                .seatCapacity(0L)
                .build();
        List<Seat> seats = List.of();
        given(concertRepository.findArea(anyLong(), anyLong(), anyLong())).willReturn(Optional.of(area));

        // when
        List<Seat> result = concertService.getAvailableSeats(1L, 1L, 1L);

        // then
        assertThat(result).isEmpty();
    }

    /**
     * 현재일시 기준 특정 콘서트의 예약 가능 공연 구역 좌석 목록을 조회한댜.
     */
    @Test
    void 현재_일시_기준_특정_콘서트의_공연_구역_좌석_목록을_조회한다_모두_예약_가능() {
        // given
        Area area = Area.builder()
                .areaId(1L)
                .concertId(1L)
                .performanceId(1L)
                .areaName("A")
                .seatPrice(BigDecimal.valueOf(10000))
                .seatCapacity(3L)
                .build();
        List<Seat> seats = List.of(
                Seat.builder()
                        .areaId(1L)
                        .concertId(1L)
                        .performanceId(1L)
                        .seatNumber("A1")
                        .seatStatus(SeatStatus.AVAILABLE)
                        .build(),
                Seat.builder()
                        .areaId(1L)
                        .concertId(1L)
                        .performanceId(1L)
                        .seatNumber("A2")
                        .seatStatus(SeatStatus.AVAILABLE)
                        .build(),
                Seat.builder()
                        .areaId(1L)
                        .concertId(1L)
                        .performanceId(1L)
                        .seatNumber("A3")
                        .seatStatus(SeatStatus.AVAILABLE)
                        .build()
        );
        given(concertRepository.findArea(anyLong(), anyLong(), anyLong())).willReturn(Optional.of(area));
        given(concertRepository.existsAvailableSeat(anyLong(), anyLong(), anyLong(), anyString())).willReturn(true);

        // when
        List<Seat> result = concertService.getAvailableSeats(1L, 1L, 1L);

        // then
        assertThat(result).hasSize(3);
    }

    /**
     * 현재일시 기준 특정 콘서트의 예약 가능 공연 구역 좌석 목록을 조회한댜.
     */
    @Test
    void 현재_일시_기준_특정_콘서트의_공연_구역_좌석_목록을_조회한다_모두_예약된_상태() {
        // given
        Area area = Area.builder()
                .areaId(1L)
                .concertId(1L)
                .performanceId(1L)
                .areaName("A")
                .seatPrice(BigDecimal.valueOf(10000))
                .seatCapacity(3L)
                .build();
        List<Seat> seats = List.of(
                Seat.builder()
                        .areaId(1L)
                        .concertId(1L)
                        .performanceId(1L)
                        .seatNumber("A1")
                        .seatStatus(SeatStatus.AVAILABLE)
                        .build(),
                Seat.builder()
                        .areaId(1L)
                        .concertId(1L)
                        .performanceId(1L)
                        .seatNumber("A2")
                        .seatStatus(SeatStatus.NOT_AVAILABLE)
                        .build(),
                Seat.builder()
                        .areaId(1L)
                        .concertId(1L)
                        .performanceId(1L)
                        .seatNumber("A3")
                        .seatStatus(SeatStatus.NOT_AVAILABLE)
                        .build()
        );
        given(concertRepository.findArea(anyLong(), anyLong(), anyLong())).willReturn(Optional.of(area));
        given(concertRepository.existsAvailableSeat(anyLong(), anyLong(), anyLong(), anyString())).willReturn(false);

        // when
        List<Seat> result = concertService.getAvailableSeats(1L, 1L, 1L);

        // then
        assertThat(result).hasSize(3);
        assertAll(() -> assertEquals(SeatStatus.NOT_AVAILABLE, result.get(1).seatStatus()),
                () -> assertEquals(SeatStatus.NOT_AVAILABLE, result.get(0).seatStatus()));
    }

    /**
     * 빈 좌석일 시 좌석을 배정한다.
     */
    @Test
    void 좌석_배정_이선좌일_시() {
        // given
        HoldSeatServiceRequest serviceRequest = HoldSeatServiceRequest.builder()
                .customerId(1L)
                .concertId(1L)
                .performanceId(1L)
                .areaId(1L)
                .seats(List.of(HoldSeatServiceRequest.SeatRequest.builder()
                                .seatNumber("A1")
                        .build()))
                .build();
        given(concertRepository.existsAvailableSeat(anyLong(), anyLong(), anyLong(), anyString())).willReturn(false);

        // when - then
        assertThatThrownBy(() -> concertService.holdSeats(serviceRequest))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.SEAT_TAKEN);
    }

    /**
     * 빈 좌석일 시 좌석을 배정한다.
     */
    @Test
    void 좌석_배정_콘서트_없을_시_예외_처리() {
        // given
        HoldSeatServiceRequest serviceRequest = HoldSeatServiceRequest.builder()
                .customerId(1L)
                .concertId(1L)
                .performanceId(1L)
                .areaId(1L)
                .seats(List.of(HoldSeatServiceRequest.SeatRequest.builder()
                        .seatNumber("A1")
                        .build()))
                .build();
        given(concertRepository.existsAvailableSeat(anyLong(), anyLong(), anyLong(), anyString())).willReturn(true);
        given(concertRepository.findConcert(anyLong())).willReturn(Optional.empty());

        // when - then
        assertThatThrownBy(() -> concertService.holdSeats(serviceRequest))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.CONCERT_NOT_FOUND);
    }

    /**
     * 빈 좌석일 시 좌석을 배정한다.
     */
    @Test
    void 좌석_배정_공연_없을_시_예외_처리() {
        // given
        HoldSeatServiceRequest serviceRequest = HoldSeatServiceRequest.builder()
                .customerId(1L)
                .concertId(1L)
                .performanceId(1L)
                .areaId(1L)
                .seats(List.of(HoldSeatServiceRequest.SeatRequest.builder()
                        .seatNumber("A1")
                        .build()))
                .build();
        Concert concert = Concert.builder()
                .concertId(1L)
                .bookBeginAt(DateUtils.createTemporalDateByIntParameters(2024,2,24,11,0,0))
                .bookEndAt(DateUtils.createTemporalDateByIntParameters(2025,2,23,11,0,0))
                .concertStatus(ConcertStatus.AVAILABLE)
                .build();
        given(concertRepository.existsAvailableSeat(anyLong(), anyLong(), anyLong(), anyString())).willReturn(true);
        given(concertRepository.findConcert(anyLong())).willReturn(Optional.of(concert));
        given(concertRepository.findPerformance(anyLong())).willReturn(Optional.empty());

        // when - then
        assertThatThrownBy(() -> concertService.holdSeats(serviceRequest))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_FOUND);
    }

    /**
     * 빈 좌석일 시 좌석을 배정한다.
     */
    @Test
    void 좌석_배정_구역_없을_시_예외_처리() {
        // given
        HoldSeatServiceRequest serviceRequest = HoldSeatServiceRequest.builder()
                .customerId(1L)
                .concertId(1L)
                .performanceId(1L)
                .areaId(1L)
                .seats(List.of(HoldSeatServiceRequest.SeatRequest.builder()
                        .seatNumber("A1")
                        .build()))
                .build();
        Concert concert = Concert.builder()
                .concertId(1L)
                .bookBeginAt(DateUtils.createTemporalDateByIntParameters(2024,2,24,11,0,0))
                .bookEndAt(DateUtils.createTemporalDateByIntParameters(2025,2,23,11,0,0))
                .concertStatus(ConcertStatus.AVAILABLE)
                .build();
        Performance performance = Performance.builder()
                .performanceId(1L)
                .concertId(1L)
                .performedAt(DateUtils.createTemporalDateByIntParameters(2025,11,13,0,0,0))
                .build();
        given(concertRepository.existsAvailableSeat(anyLong(), anyLong(), anyLong(), anyString())).willReturn(true);
        given(concertRepository.findConcert(anyLong())).willReturn(Optional.of(concert));
        given(concertRepository.findPerformance(anyLong())).willReturn(Optional.of(performance));
        given(concertRepository.findArea(anyLong())).willReturn(Optional.empty());

        // when - then
        assertThatThrownBy(() -> concertService.holdSeats(serviceRequest))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_FOUND);
    }

    /**
     * 빈 좌석일 시 좌석을 배정한다.
     */
    @Test
    void 좌석_배정_성공() {
        // given
        HoldSeatServiceRequest serviceRequest = HoldSeatServiceRequest.builder()
                .customerId(1L)
                .concertId(1L)
                .performanceId(1L)
                .areaId(1L)
                .seats(List.of(HoldSeatServiceRequest.SeatRequest.builder()
                        .seatNumber("A1")
                        .build()))
                .build();
        Concert concert = Concert.builder()
                .concertId(1L)
                .concertName("항해콘서트")
                .artistName("김항해")
                .bookBeginAt(DateUtils.createTemporalDateByIntParameters(2024,2,24,11,0,0))
                .bookEndAt(DateUtils.createTemporalDateByIntParameters(2025,2,23,11,0,0))
                .concertStatus(ConcertStatus.AVAILABLE)
                .build();
        Performance performance = Performance.builder()
                .performanceId(1L)
                .concertId(1L)
                .performedAt(DateUtils.createTemporalDateByIntParameters(2025,11,13,0,0,0))
                .build();
        Area area = Area.builder()
                .areaId(1L)
                .areaName("A")
                .seatCapacity(50L)
                .seatPrice(BigDecimal.valueOf(30000))
                .build();
        List<HeldSeatServiceResponse> serviceResponse = List.of(
                HeldSeatServiceResponse.builder()
                        .reservationId(1L)
                        .ticketId(1L)
                        .concertId(1L)
                        .performanceId(1L)
                        .areaId(1L)
                        .seatNumber("A1")
                        .seatStatus(SeatStatus.WAITING_FOR_RESERVATION)
                        .build()
        );
        Reservation reservation = Reservation.builder()
                .reservationId(1L)
                .customerId(1L)
                .build();
        Ticket ticket = Ticket.builder()
                .ticketId(1L)
                .reservationId(1L)
                .concertId(1L)
                .performanceId(1L)
                .areaId(1L)
                .concertName("항해콘서트")
                .artistName("김항해")
                .performedAt(DateUtils.createTemporalDateByIntParameters(2025,11,13,0,0,0))
                .areaName("A")
                .seatNumber("A1")
                .ticketPrice(BigDecimal.valueOf(30000))
                .build();

        given(concertRepository.existsAvailableSeat(anyLong(), anyLong(), anyLong(), anyString())).willReturn(true);
        given(concertRepository.findConcert(anyLong())).willReturn(Optional.of(concert));
        given(concertRepository.findPerformance(anyLong())).willReturn(Optional.of(performance));
        given(concertRepository.findArea(anyLong())).willReturn(Optional.of(area));
        given(concertRepository.saveReservation(any(Reservation.class))).willReturn(reservation);
        given(concertRepository.saveTicket(any(Ticket.class))).willReturn(ticket);

        // when
        List<HeldSeatServiceResponse> result = concertService.holdSeats(serviceRequest);

        // then
        assertAll(() -> assertThat(result).isNotEmpty(),
                () -> assertEquals("A1", result.get(0).seatNumber()),
                () -> assertEquals(SeatStatus.WAITING_FOR_RESERVATION, result.get(0).seatStatus()));
    }
}