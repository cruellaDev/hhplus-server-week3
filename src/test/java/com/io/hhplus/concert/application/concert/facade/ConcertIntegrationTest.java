package com.io.hhplus.concert.application.concert.facade;

import com.io.hhplus.concert.application.concert.dto.PerformancesResponse;
import com.io.hhplus.concert.application.concert.dto.SeatsResponse;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.IllegalArgumentCustomException;
import com.io.hhplus.concert.common.exceptions.ResourceNotFoundCustomException;
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
     * 예약 가능 콘서트 공연 목록 조회 - 콘서트, 공연 모두 존재
     */
    @Test
    void getAvailablePerformances_when_all_exists() {
        // given
        long concertId = 1;

        // when
        PerformancesResponse result = concertFacade.getAvailablePerformances(concertId);

        // then
        assertAll(() -> assertEquals(concertId, result.getConcert().concertId()),
                () -> assertFalse(result.getPerformances().isEmpty()));
    }

    /**
     * 예약 가능 콘서트 공연 목록 조회 - 이상한 콘서트_ID 입력
     */
    @Test
    void getAvailablePerformances_when_concertId_is_invalid() {
        // given
        long concertId = -3;

        // when - then
        assertThatThrownBy(() -> concertFacade.getAvailablePerformances(concertId))
                .isInstanceOf(IllegalArgumentCustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.INVALID);
    }

    /**
     * 예약 가능 콘서트 공연 목록 조회 - 존재하지 않는 콘서트_ID 입력
     */
    @Test
    void getAvailablePerformances_when_concert_is_not_exists() {
        // given
        long concertId = 100001;

        // when - then
        assertThatThrownBy(() -> concertFacade.getAvailablePerformances(concertId))
                .isInstanceOf(ResourceNotFoundCustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_AVAILABLE);
    }

    /**
     * 예약 가능 콘서트 공연 목록 조회 - 콘서트는 존재하나 공연이 존재하지 않으면 빈 배열 리턴
     */
    @Test
    void getAvailablePerformances_when_performance_not_exists() {
        // given
        long concertId = 2;

        // when
        PerformancesResponse result = concertFacade.getAvailablePerformances(concertId);

        // then
        assertAll(() -> assertEquals(concertId, result.getConcert().concertId()),
                () -> assertTrue(result.getPerformances().isEmpty()));
    }

    @Test
    void getAvailableSeats_when_all_exists() {
        // given
        long concertId = 1;
        long performanceId = 1;

        // when
        SeatsResponse result = concertFacade.getAvailableSeats(concertId, performanceId);

        // then
        assertAll(() -> assertEquals(concertId, result.getConcert().concertId()),
                () -> assertEquals(performanceId, result.getPerformance().performanceId()),
                () -> assertFalse(result.getSeats().isEmpty()));
    }

    /**
     * 예약 가능 콘서트 공연 좌석 목록 조회 - 좌석만 없는 경우
     */
    @Test
    void getAvailableSeats_when_seats_not_exist() {
        // given
        long concertId = 3;
        long performanceId = 2;

        // when
        SeatsResponse result = concertFacade.getAvailableSeats(concertId, performanceId);

        // then
        assertAll(() -> assertEquals(concertId, result.getConcert().concertId()),
                () -> assertEquals(performanceId, result.getPerformance().performanceId()),
                () -> assertTrue(result.getSeats().isEmpty()));
    }

    /**
     * 예약 가능 콘서트 공연 좌석 목록 조회 - 콘서트가 없는 경우
     */
    @Test
    void getAvailableSeats_when_concert_not_exists() {
        // given
        long concertId = 999;
        long performanceId = 2;

        // when - then
        assertThatThrownBy(() -> concertFacade.getAvailableSeats(concertId, performanceId))
                .isInstanceOf(ResourceNotFoundCustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_AVAILABLE);
    }

    /**
     * 예약 가능 콘서트 공연 좌석 목록 조회 - 공연이 없는 경우
     */
    @Test
    void getAvailableSeats_when_performance_not_exists() {
        // given
        long concertId = 1;
        long performanceId = 10001;

        // when - then
        assertThatThrownBy(() -> concertFacade.getAvailableSeats(concertId, performanceId))
                .isInstanceOf(ResourceNotFoundCustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_AVAILABLE);
    }

    /**
     * 예약 가능 콘서트 공연 좌석 목록 조회 - 공연이 데이터는 존재하지만 공연일시가 지나 예약이 불가능한 경우
     */
    @Test
    void getAvailableSeats_when_performance_invalid_performAt() {
        // given
        long concertId = 1;
        long performanceId = 3;

        // when - then
        assertThatThrownBy(() -> concertFacade.getAvailableSeats(concertId, performanceId))
                .isInstanceOf(ResourceNotFoundCustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_AVAILABLE);
    }

}