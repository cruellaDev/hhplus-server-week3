package com.io.hhplus.concert.application.concert.facade;

import com.io.hhplus.concert.application.concert.dto.PerformancesResponse;
import com.io.hhplus.concert.application.concert.dto.SeatsResponse;
import com.io.hhplus.concert.domain.concert.model.Concert;
import com.io.hhplus.concert.domain.concert.model.Performance;
import com.io.hhplus.concert.domain.concert.model.Seat;
import com.io.hhplus.concert.domain.concert.service.ConcertService;
import com.io.hhplus.concert.domain.concert.service.PerformanceService;
import com.io.hhplus.concert.domain.concert.service.SeatService;
import com.io.hhplus.concert.common.exceptions.IllegalArgumentCustomException;
import com.io.hhplus.concert.common.exceptions.ResourceNotFoundCustomException;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertFacade {

    private final ConcertService concertService;
    private final PerformanceService performanceService;
    private final SeatService seatService;

    /**
     * 예약 가능 콘서트 공연 목록 조회
     * @param concertId 콘서트_ID
     * @return 응답 정보
     */
    public PerformancesResponse getAvailablePerformances(Long concertId) {
        if (!Concert.isAvailableConcertId(concertId)) {
            throw new IllegalArgumentCustomException("콘서트 ID 값이 잘못 되었습니다.", ResponseMessage.INVALID);
        }

        Concert concert;
        List<Performance> performances;

        // 콘서트 조회
        concert = concertService.getAvailableConcert(concertId);
        // 콘서트 검증
        boolean isReservableConcert = concertService.meetsIfConcertToBeReserved(concert);
        if (!isReservableConcert) {
            throw new ResourceNotFoundCustomException("예약 가능한 콘서트가 존재하지 않습니다.", ResponseMessage.NOT_AVAILABLE);
        }

        // 공연 조회 (없을 시 빈 리스트)
        performances = performanceService.getAvailablePerformances(concertId);

        // response 반환
        return new PerformancesResponse(concert, performances);
    }

    /**
     * 예약 가능 콘서트 공연 좌석 목록 조회
     * @param concertId 콘서트_ID
     * @param performanceId 공연_ID
     * @return 응답 정보
     */
    public SeatsResponse getAvailableSeats(Long concertId, Long performanceId) {
        if (!Concert.isAvailableConcertId(concertId)) {
            throw new IllegalArgumentCustomException("콘서트 ID 값이 잘못 되었습니다.", ResponseMessage.INVALID);
        }
        if (!Performance.isAvailablePerformanceId(performanceId)) {
            throw new IllegalArgumentCustomException("공연 ID 값이 잘못 되었습니다.", ResponseMessage.INVALID);
        }

        boolean isReservable;
        Concert concert;
        Performance performance;
        List<Seat> seats;

        // 콘서트 조회
        concert = concertService.getAvailableConcert(concertId);
        // 콘서트 예약 가능 검증
        isReservable = concertService.meetsIfConcertToBeReserved(concert);
        if (!isReservable) {
            throw new ResourceNotFoundCustomException("예약 가능한 콘서트가 존재하지 않습니다.", ResponseMessage.NOT_AVAILABLE);
        }

        // 공연 조회
        performance = performanceService.getAvailablePerformance(concertId, performanceId);
        // 공연 예약 가능 검증
        // - 현재 일시 기준 예약 가능한 공연
        isReservable = performanceService.meetsIfPerformanceToBeReserved(performance);
        if (!isReservable) {
            throw new ResourceNotFoundCustomException("예약 가능한 공연이 존재하지 않습니다.", ResponseMessage.NOT_AVAILABLE);
        }

        // 좌석 조회
        // 예약 가능한 좌석이 없을 시 빈 리스트 반환
        seats = seatService.getAvailableSeats(performanceId);

        // response 반환
        return new SeatsResponse(concert, performance, seats);
    }
}
