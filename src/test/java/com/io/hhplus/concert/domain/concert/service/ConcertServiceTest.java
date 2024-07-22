package com.io.hhplus.concert.domain.concert.service;

import com.io.hhplus.concert.common.enums.SeatStatus;
import com.io.hhplus.concert.common.utils.DateUtils;
import com.io.hhplus.concert.domain.concert.model.Concert;
import com.io.hhplus.concert.domain.concert.model.Performance;
import com.io.hhplus.concert.domain.concert.model.Seat;
import com.io.hhplus.concert.domain.concert.repository.ConcertRepository;
import com.io.hhplus.concert.domain.concert.repository.PerformanceRepository;
import com.io.hhplus.concert.domain.concert.repository.SeatRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ConcertServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @Mock
    private PerformanceRepository performanceRepository;

    @Mock
    private SeatRepository seatRepository;

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
        given(performanceRepository.findPerformances(anyLong())).willReturn(performances);

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
        given(performanceRepository.findPerformances(anyLong())).willReturn(performances);

        // when
        List<Performance> result = concertService.getAvailablePerformances(999L);

        // then
        assertThat(result).hasSize(0);
    }

    /**
     * 현재일시 기준 특정 콘서트의 공연 좌석 목록을 조회한다.
     */
    @Test
    void 현재_일시_기준_특정_콘서트의_공연_좌석_목록을_조회한다() {
        // given
        List<Seat> seats = List.of(
                Seat.builder()
                    .seatId(1L)
                    .performanceId(1L)
                    .concertId(1L)
                    .seatStatus(SeatStatus.AVAILABLE)
                    .build()
        );
        given(seatRepository.findSeats(anyLong(), anyLong())).willReturn(seats);

        // when
        List<Seat> result = concertService.getAvailableSeats(1L, 1L);

        // then
        assertThat(result).hasSize(1);
    }

    /**
     * 현재일시 기준 특정 콘서트의 공연 좌석 목록을 조회한다.
     */
    @Test
    void 현재_일시_기준_특정_콘서트의_공연_좌석_목록이_없으면_빈배열을_반환한다() {
        // given
        List<Seat> seats = List.of();
        given(seatRepository.findSeats(anyLong(), anyLong())).willReturn(seats);

        // when
        List<Seat> result = concertService.getAvailableSeats(1L, 1L);

        // then
        assertThat(result).hasSize(0);
    }
}