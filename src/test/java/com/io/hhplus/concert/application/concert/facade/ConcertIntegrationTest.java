package com.io.hhplus.concert.application.concert.facade;

import com.io.hhplus.concert.application.concert.dto.ConcertInfoWithPerformance;
import com.io.hhplus.concert.application.concert.dto.ConcertInfoWithPerformanceAndSeats;
import com.io.hhplus.concert.application.concert.dto.ConcertsInfo;
import com.io.hhplus.concert.common.enums.ConcertStatus;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ConcertIntegrationTest {

    @Autowired
    private ConcertFacade concertFacade;

    /**
     * 예약 가능 콘서트 목록 조회 - 콘서트 존재
     */
    @Test
    void 예약_가능_콘서트_목록을_모두_조회한다() {
        // given
        long concertId = 1;
        ConcertStatus concertStatus = ConcertStatus.AVAILABLE;

        // when
        ConcertsInfo result = concertFacade.getAvailableConcerts();

        // then
        assertAll(() -> assertEquals(concertId, result.getConcerts().get(0).concertId()),
                () -> assertEquals(concertStatus, result.getConcerts().get(0).concertStatus()));
    }

    /**
     * 예약 가능 콘서트 공연 목록 조회 - 콘서트, 공연 모두 존재
     */
    @Test
    void 예약_가능_콘서트_공연_목록_조회_콘서트와_공연_데이터가_모두_존재할_경우_데이터를_조회하여_모두_반환한다() {
        // given
        long concertId = 1;

        // when
        ConcertInfoWithPerformance result = concertFacade.getAvailablePerformances(concertId);

        // then
        assertAll(() -> assertEquals(concertId, result.getConcert().concertId()),
                () -> assertFalse(result.getPerformances().isEmpty()));
    }

    /**
     * 예약 가능 콘서트 공연 목록 조회 - 이상한 콘서트_ID 입력
     */
    @Test
    void 예약_가능_콘서트_공연_목록_조회_콘서트ID가_null_일_경우_예외_처리() {
        // given
        Long concertId = null;

        // when - then
        assertThatThrownBy(() -> concertFacade.getAvailablePerformances(concertId))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.INVALID);
    }

    /**
     * 예약 가능 콘서트 공연 목록 조회 - 이상한 콘서트_ID 입력
     */
    @Test
    void 예약_가능_콘서트_공연_목록_조회_콘서트ID가_음의_정수_일_경우_예외_처리() {
        // given
        long concertId = -3;

        // when - then
        assertThatThrownBy(() -> concertFacade.getAvailablePerformances(concertId))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_AVAILABLE);
    }

    /**
     * 예약 가능 콘서트 공연 목록 조회 - 이상한 콘서트_ID 입력
     */
    @Test
    void 예약_가능_콘서트_공연_목록_조회_콘서트ID가_0_일_경우_예외_처리() {
        // given
        long concertId = 0;

        // when - then
        assertThatThrownBy(() -> concertFacade.getAvailablePerformances(concertId))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_AVAILABLE);
    }

    /**
     * 예약 가능 콘서트 공연 목록 조회 - 존재하지 않는 콘서트_ID 입력
     */
    @Test
    void 예약_가능_콘서트_공연_목록_조회_주어진_콘서트ID를_가진_콘서트가_존재하지_않을_경우_예외_처리() {
        // given
        long concertId = 100001;

        // when - then
        assertThatThrownBy(() -> concertFacade.getAvailablePerformances(concertId))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_AVAILABLE);
    }

    /**
     * 예약 가능 콘서트 공연 목록 조회 - 콘서트는 존재하나 공연이 존재하지 않으면 빈 배열 리턴
     */
    @Test
    void 예약_가능_콘서트_공연_목록_조회_주어진_콘서트ID를_가진_콘서트가_존재하지만_공연이_없을_경우_빈_배열을_반환_한다() {
        // given
        long concertId = 2;

        // when
        ConcertInfoWithPerformance result = concertFacade.getAvailablePerformances(concertId);

        // then
        assertAll(() -> assertEquals(concertId, result.getConcert().concertId()),
                () -> assertTrue(result.getPerformances().isEmpty()));
    }

    /**
     * 예약 가능 콘서트 공연 좌석 목록 조회 - 좌석만 모두 조회
     */
    @Test
    void 예약_가능_콘서트_공연_좌석_목록_조회_콘서트_공연_좌석_모두_존재할_시_조회된_값을_모두_반환한다() {
        // given
        long concertId = 1;
        long performanceId = 1;

        // when
        ConcertInfoWithPerformanceAndSeats result = concertFacade.getAvailableSeats(concertId, performanceId);

        // then
        assertAll(() -> assertEquals(concertId, result.getConcert().concertId()),
                () -> assertEquals(performanceId, result.getPerformance().performanceId()),
                () -> assertFalse(result.getSeats().isEmpty()));
    }

    /**
     * 예약 가능 콘서트 공연 좌석 목록 조회 - 좌석만 없는 경우
     */
    @Test
    void 예약_가능_콘서트_공연_좌석_목록_조회_콘서트_공연_모두_존재하나_좌석이_존재하지_않을_시_좌석은_빈_배열을_반환한다() {
        // given
        long concertId = 3;
        long performanceId = 2;

        // when
        ConcertInfoWithPerformanceAndSeats result = concertFacade.getAvailableSeats(concertId, performanceId);

        // then
        assertAll(() -> assertEquals(concertId, result.getConcert().concertId()),
                () -> assertEquals(performanceId, result.getPerformance().performanceId()),
                () -> assertTrue(result.getSeats().isEmpty()));
    }

    /**
     * 예약 가능 콘서트 공연 좌석 목록 조회 - 콘서트_ID가 이상할 시
     */
    @Test
    void 예약_가능_콘서트_공연_좌석_목록_조회_콘서트ID가_null_일_시_예외_처리() {
        // given
        Long concertId = null;
        long performanceId = 2;

        // when - then
        assertThatThrownBy(() -> concertFacade.getAvailableSeats(concertId, performanceId))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.INVALID);
    }

    /**
     * 예약 가능 콘서트 공연 좌석 목록 조회 - 콘서트가 없는 경우
     */
    @Test
    void 예약_가능_콘서트_공연_좌석_목록_조회_콘서트가_존재하지_않을_시_예외_처리() {
        // given
        long concertId = 999;
        long performanceId = 2;

        // when - then
        assertThatThrownBy(() -> concertFacade.getAvailableSeats(concertId, performanceId))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_AVAILABLE);
    }

    /**
     * 예약 가능 콘서트 공연 좌석 목록 조회 - 공연_ID가 이상할 시
     */
    @Test
    void 예약_가능_콘서트_공연_좌석_목록_조회_공연ID가_null_일_시_예외_처리() {
        // given
        long concertId = 1;
        Long performanceId = null;

        // when - then
        assertThatThrownBy(() -> concertFacade.getAvailableSeats(concertId, performanceId))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.INVALID);
    }

    /**
     * 예약 가능 콘서트 공연 좌석 목록 조회 - 공연이 없는 경우
     */
    @Test
    void 예약_가능_콘서트_공연_좌석_목록_조회_공연이_존재하지_않을_시_예외_처리() {
        // given
        long concertId = 1;
        long performanceId = 10001;

        // when - then
        assertThatThrownBy(() -> concertFacade.getAvailableSeats(concertId, performanceId))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_AVAILABLE);
    }

    /**
     * 예약 가능 콘서트 공연 좌석 목록 조회 - 공연이 데이터는 존재하지만 공연일시가 지나 예약이 불가능한 경우
     */
    @Test
    void 예약_가능_콘서트_공연_좌석_목록_조회_공연의_공연일시가_이미_지난경우_예외처리() {
        // given
        long concertId = 1;
        long performanceId = 3;

        // when - then
        assertThatThrownBy(() -> concertFacade.getAvailableSeats(concertId, performanceId))
                .isInstanceOf(CustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_AVAILABLE);
    }

}