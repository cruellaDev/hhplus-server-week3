package com.io.hhplus.concert.integration;

import com.io.hhplus.concert.common.enums.ConcertStatus;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.common.utils.DataBaseCleanUp;
import com.io.hhplus.concert.common.utils.TestDataInitializer;
import com.io.hhplus.concert.domain.concert.ConcertCommand;
import com.io.hhplus.concert.domain.concert.ConcertRepository;
import com.io.hhplus.concert.domain.concert.dto.AvailableSeatInfo;
import com.io.hhplus.concert.domain.concert.model.*;
import com.io.hhplus.concert.application.concert.ConcertFacade;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // @DirtiesContext 컨텍스트의 상태를 초기화
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ConcertIntegrationTest {

    @Autowired
    TestDataInitializer testDataInitializer;

    @Autowired
    private DataBaseCleanUp dataBaseCleanUp;

    @Autowired
    private ConcertFacade concertFacade;

    @Autowired
    private ConcertRepository concertRepository;

    Logger logger = LoggerFactory.getLogger(ConcertIntegrationTest.class);

    @BeforeEach
    void setUp() {
        testDataInitializer.initializeTestData();

        Concert concert = concertRepository.findConcert(2L).orElse(null);
        ConcertSchedule concertSchedule = concertRepository.findConcertSchedule(2L, 13L).orElse(null);
        ConcertSeat concertSeat = concertRepository.findConcertSeat(2L, 13L).orElse(null);


        Reservation reservation = concertRepository.saveReservation(
                Reservation.builder()
                        .customerId(1L)
                        .bookerName("고객1")
                        .build()
        );

        Ticket ticket = concertRepository.saveTicket(
                Ticket.builder()
                        .reservationId(reservation.reservationId())
                        .concertId(concert.concertId())
                        .concertScheduleId(concertSchedule.concertScheduleId())
                        .concertSeatId(concertSeat.concertSeatId())
                        .concertName(concert.concertName())
                        .artistName(concert.artistName())
                        .performedAt(concertSchedule.performedAt())
                        .ticketPrice(concertSeat.seatPrice())
                        .seatNumber("30")
                        .build()
        );
    }

    @AfterEach
    void tearDown() {
        dataBaseCleanUp.execute();
    }

    /**
     * 예약 가능 콘서트 목록 조회 - 콘서트 존재
     */
    @Test
    void 예약_가능_콘서트_목록을_모두_조회한다() {
        // given
        long concertId = 2;
        ConcertStatus concertStatus = ConcertStatus.AVAILABLE;

        // when
        List<Concert> result = concertFacade.getAvailableConcerts();

        // then
        assertAll(() -> assertFalse(result.isEmpty()),
                () -> assertEquals(concertId, result.get(1).concertId()),
                () -> assertEquals(concertStatus, result.get(1).concertStatus()));
    }

    /**
     * 예약 가능 콘서트 일정 목록 조회 - 콘서트, 일정 모두 존재
     */
    @Test
    void 예약_가능_콘서트_일정_목록_조회_콘서트와_일정_데이터가_모두_존재할_경우_데이터를_조회하여_모두_반환한다() {
        // given
        long concertId = 2;

        // when
        List<ConcertSchedule> result = concertFacade.getAvailableSchedules(concertId);

        // then
        assertAll(() -> assertFalse(result.isEmpty()),
                () -> assertEquals(concertId, result.get(1).concertId()));
    }

    /**
     * 예약 가능 콘서트 일정 목록 조회 - 이상한 콘서트_ID 입력
     */
    @Test
    void 예약_가능_콘서트_일정_목록_조회_콘서트_ID가_null_일_경우_빈_리스트를_반환한다() {
        // given
        Long concertId = null;

        // when
        List<ConcertSchedule> result = concertFacade.getAvailableSchedules(concertId);

        // then
        assertAll(() -> assertTrue(result.isEmpty()));
    }

    /**
     * 예약 가능 콘서트 일정 목록 조회 - 이상한 콘서트_ID 입력
     */
    @Test
    void 예약_가능_콘서트_일정_목록_조회_콘서트ID가_음의_정수_일_경우_빈_리스트를_반환한다() {
        // given
        long concertId = -3;

        // when
        List<ConcertSchedule> result = concertFacade.getAvailableSchedules(concertId);

        // then
        assertAll(() -> assertTrue(result.isEmpty()));
    }

    /**
     * 예약 가능 콘서트 일정 목록 조회 - 이상한 콘서트_ID 입력
     */
    @Test
    void 예약_가능_콘서트_일정_목록_조회_콘서트ID가_0_일_경우_빈_리스트를_반환한다() {
        // given
        long concertId = 0;

        // when
        List<ConcertSchedule> result = concertFacade.getAvailableSchedules(concertId);

        // then
        assertAll(() -> assertTrue(result.isEmpty()));
    }

    /**
     * 예약 가능 콘서트 일정 목록 조회 - 존재하지 않는 콘서트_ID 입력
     */
    @Test
    void 예약_가능_콘서트_일정_목록_조회_주어진_콘서트ID를_가진_콘서트가_존재하지_않을_경우_빈_리스트를_반환한다() {
        // given
        long concertId = 900001;

        // when
        List<ConcertSchedule> result = concertFacade.getAvailableSchedules(concertId);

        // then
        assertAll(() -> assertTrue(result.isEmpty()));
    }

    /**
     * 예약 가능 콘서트 일정 좌석 목록 조회 - 좌석만 모두 조회
     */
    @Test
    void 예약_가능_콘서트_일정_좌석_목록_조회_콘서트_일정_좌석_모두_존재할_시_조회된_값을_모두_반환한다() {
        // given
        long concertId = 10;
        long concertScheduleId = 98;

        // when
        List<AvailableSeatInfo> result = concertFacade.getAvailableSeats(concertId, concertScheduleId);

        // then
        assertAll(() -> assertFalse(result.isEmpty()),
                () -> assertEquals(concertId, result.get(0).concertId()),
                () -> assertEquals(concertScheduleId, result.get(0).concertScheduleId()));
    }

    /**
     * 예약 가능 콘서트 일정 좌석 목록 조회 - 콘서트_ID가 이상할 시
     */
    @Test
    void 예약_가능_콘서트_일정_좌석_목록_조회_콘서트ID가_null_일_시_예외를_반환한다() {
        // given
        Long concertId = null;
        long concertScheduleId = 2;

        // when - then
        assertThatThrownBy(() -> concertFacade.getAvailableSeats(concertId, concertScheduleId))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.CONCERT_SEAT_NOT_FOUND);
    }

    /**
     * 예약 가능 콘서트 일정 좌석 목록 조회 - 콘서트가 없는 경우
     */
    @Test
    void 예약_가능_콘서트_일정_좌석_목록_조회_콘서트가_존재하지_않을_시_예외를_반환한다() {
        // given
        long concertId = 999;
        long concertScheduleId = 2;

        // when - then
        assertThatThrownBy(() -> concertFacade.getAvailableSeats(concertId, concertScheduleId))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.CONCERT_SEAT_NOT_FOUND);
    }

    /**
     * 예약 가능 콘서트 일정 좌석 목록 조회 - 일정_ID가 이상할 시
     */
    @Test
    void 예약_가능_콘서트_일정_좌석_목록_조회_일정ID가_null_일_시_예외를_반환한다() {
        // given
        long concertId = 1;
        Long concertScheduleId = null;

        // when - then
        assertThatThrownBy(() -> concertFacade.getAvailableSeats(concertId, concertScheduleId))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.CONCERT_SEAT_NOT_FOUND);
    }

    /**
     * 예약 가능 콘서트 일정 좌석 목록 조회 - 일정이 없는 경우
     */
    @Test
    void 예약_가능_콘서트_일정_좌석_목록_조회_일정이_존재하지_않을_시_예외를_반환한다() {
        // given
        long concertId = 1;
        long performanceId = 10001;

        // when - then
        assertThatThrownBy(() -> concertFacade.getAvailableSeats(concertId, performanceId))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.CONCERT_SEAT_NOT_FOUND);
    }

    @Test
    void 좌석_배정_및_예약_요청_시_빈_좌석이_아닐_경우_예외를_반환한다() {
        // given
        long concertId = 2;
        long concertScheduleId = 13;
        long concertSeatId = 13;

        // when - then
        ConcertCommand.ReserveSeatsCommand command = ConcertCommand.ReserveSeatsCommand.builder()
                .customerId(3L)
                .concertId(concertId)
                .concertScheduleId(concertScheduleId)
                .concertSeatId(concertSeatId)
                .bookerName("김익명")
                .seatNumbers(List.of("30"))
                .build();
        assertThatThrownBy(() -> concertFacade.reserveSeats(command))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.SEAT_TAKEN);
    }

}