package com.io.hhplus.concert.domain.concert.service;

import com.io.hhplus.concert.common.enums.ConcertStatus;
import com.io.hhplus.concert.common.enums.SeatStatus;
import com.io.hhplus.concert.common.utils.DateUtils;
import com.io.hhplus.concert.domain.concert.service.model.ConcertModel;
import com.io.hhplus.concert.domain.concert.service.model.PerformanceModel;
import com.io.hhplus.concert.domain.concert.service.model.SeatModel;
import com.io.hhplus.concert.domain.concert.repository.ConcertRepository;
import com.io.hhplus.concert.domain.concert.repository.PerformanceRepository;
import com.io.hhplus.concert.domain.concert.repository.SeatRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    void 예약_콘서트_목록을_조회한다() {
        // given
        List<ConcertModel> concertModels = List.of(ConcertModel.create(1L, "하헌우코치님 팬미팅", "하헌우", ConcertStatus.AVAILABLE));
        given(concertRepository.findAvailableAll()).willReturn(concertModels);

        // when
        List<ConcertModel> result = concertService.getAvailableConcerts();

        // then
        assertThat(result).hasSize(1);
    }

    /**
     * 예약 가능 콘서트 단건 조회
     */
    @Test
    void 예약_가능_콘서트_단건을_조회한다() {
        // given
        ConcertModel concertModel = ConcertModel.create(1L, "허재코치님 팬미팅", "허재", ConcertStatus.AVAILABLE);
        given(concertRepository.findAvailableOneById(anyLong())).willReturn(Optional.of(concertModel));

        // when
        ConcertModel result = concertService.getAvailableConcert(1L);

        // then
        assertAll(() -> assertEquals(concertModel.concertId(), result.concertId()),
                () -> assertEquals(concertModel.concertName(), result.concertName()),
                () -> assertEquals(concertModel.artistName(), result.artistName()),
                () -> assertEquals(concertModel.concertStatus(), result.concertStatus()));
    }
    /**
     * 공연 목록 반환
     */
    @Test
    void getAvailablePerformances() {
        //given
        List<PerformanceModel> performanceModels = List.of(
                PerformanceModel.create(1L, BigDecimal.valueOf(30000), 50, DateUtils.createTemporalDateByIntParameters(2024, 9, 22, 13, 0, 0)),
                PerformanceModel.create(2L, BigDecimal.valueOf(100000), 50, DateUtils.createTemporalDateByIntParameters(2024, 12, 22, 13, 0, 0))
        );
        given(performanceRepository.findAvailableAllByConcertId(anyLong())).willReturn(performanceModels);

        //when
        List<PerformanceModel> result = concertService.getAvailablePerformances(3L);

        //then
        assertThat(result).hasSize(2);
    }

    /**
     * 공연 단건 반환
     */
    @Test
    void getAvailablePerformance() {
        PerformanceModel performanceModel = PerformanceModel.create(1L, BigDecimal.valueOf(30000), 50, DateUtils.createTemporalDateByIntParameters(2024, 9, 22, 13, 0, 0));

        given(performanceRepository.findAvailableOneByConcertIdAndPerformanceId(anyLong(), anyLong())).willReturn(Optional.of(performanceModel));

        //when
        PerformanceModel result = concertService.getAvailablePerformance(3L, 4L);

        //then
        assertAll(() -> assertEquals(performanceModel.performanceId(), result.performanceId()),
                () -> assertEquals(performanceModel.price(), result.price()),
                () -> assertEquals(performanceModel.capacityLimit(), result.capacityLimit()),
                () -> assertEquals(performanceModel.performedAt(), result.performedAt()));
    }

    /**
     * 예약 가능 콘서트 공연 좌석 목록 조회 테스트
     */
    @Test
    void getAvailableSeats() {
        // given
        List<SeatModel> seatModels = List.of(
                SeatModel.create(1L, 1L, 1L, "50", SeatStatus.AVAILABLE),
                SeatModel.create(2L, 1L, 1L, "49", SeatStatus.AVAILABLE)
        );
        given(seatRepository.findAvailableAllByPerformanceId(anyLong())).willReturn(seatModels);

        // when
        List<SeatModel> result = concertService.getAvailableSeats(1L);

        // then
        assertThat(result).hasSize(2);
    }

    /**
     * 예약 가능 콘서트 공연 좌석 단건 조회
     */
    @Test
    void getAvailableSeat() {
        // given
        SeatModel seatModel = SeatModel.create(1L, 1L, 1L, "50", SeatStatus.AVAILABLE);
        given(seatRepository.findAvailableOneByPerformanceIdAndSeatId(anyLong(), anyLong())).willReturn(Optional.of(seatModel));

        // then
        SeatModel result = concertService.getAvailableSeat(1L, 1L);

        // then
        assertAll(() -> assertEquals(seatModel.seatId(), result.seatId()),
                () -> assertEquals(seatModel.performanceId(), result.performanceId()),
                () -> assertEquals(seatModel.concertId(), result.concertId()),
                () -> assertEquals(seatModel.seatNo(), result.seatNo()),
                () -> assertEquals(seatModel.seatStatus(), result.seatStatus()));
    }

    /**
     * 좌석 임시 배정 - 빈 좌석일 시
     */
    @Test
    void assignSeatTemporarily() {
        // given
        SeatModel asisSeatModel = SeatModel.create(1L, 1L, 1L, "50", SeatStatus.AVAILABLE);
        SeatModel tobeSeatModel = SeatModel.create(1L, 1L, 1L, "50", SeatStatus.WAITING_FOR_RESERVATION);
        given(seatRepository.findById(anyLong())).willReturn(Optional.of(asisSeatModel));
        given(seatRepository.save(any(SeatModel.class))).willReturn(tobeSeatModel);

        // when
        SeatModel result = concertService.assignSeatTemporarily(1L);

        // then
        assertThat(result.seatStatus()).isEqualTo(tobeSeatModel.seatStatus());
    }

    /**
     * 좌석 임시 배정 상태변경
     */
    @Test
    void updateSeatStatusByReservationIdAndReservationStatus() {
        // given
        List<SeatModel> asisSeatModels = List.of(
                SeatModel.create(1L, 1L, 1L, "50", SeatStatus.WAITING_FOR_RESERVATION),
                SeatModel.create(2L, 1L, 1L, "49", SeatStatus.WAITING_FOR_RESERVATION)
        );
        List<SeatModel> tobeSeatModels = List.of(
                SeatModel.create(1L, 1L, 1L, "50", SeatStatus.AVAILABLE),
                SeatModel.create(2L, 1L, 1L, "49", SeatStatus.AVAILABLE)
        );
        given(seatRepository.findAllByReservationIdAndSeatStatus(anyLong(), any())).willReturn(asisSeatModels);
        given(seatRepository.save(any(SeatModel.class))).willReturn(tobeSeatModels.get(0));
        given(seatRepository.save(any(SeatModel.class))).willReturn(tobeSeatModels.get(1));

        // when
        List<SeatModel> result = concertService.updateSeatStatusByReservationIdAndReservationStatus(1L, SeatStatus.WAITING_FOR_RESERVATION, SeatStatus.AVAILABLE);

        // then
        assertAll(() -> assertEquals(result.get(0).seatStatus(), SeatStatus.AVAILABLE),
                () -> assertEquals(result.get(0).seatStatus(), SeatStatus.AVAILABLE));
    }
}