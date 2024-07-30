package com.io.hhplus.concert.application.concert.facade;

import com.io.hhplus.concert.application.concert.dto.*;
import com.io.hhplus.concert.domain.concert.service.ConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertFacade {

    private final ConcertService concertService;

    /**
     * 예약 가능한 콘서트 목록 조회
     * @return 콘서트 목록
     */
    public List<ConcertServiceResponse> getAvailableConcerts() {
        return concertService.getConcerts()
                .stream()
                .map(ConcertServiceResponse::from)
                .toList();
    }

    /**
     * 예약 가능 콘서트 공연 목록 조회
     * @param concertId 콘서트_ID
     * @return 응답 정보
     */
    public List<PerformanceServiceResponse> getAvailablePerformances(Long concertId) {
        return concertService.getAvailablePerformances(concertId)
                .stream()
                .map(PerformanceServiceResponse::from)
                .toList();
    }

    /**
     * 예약 가능 콘서트 공연 구역 목록 조회
     * @param concertId 콘서트_ID
     * @param performanceId 공연_ID
     * @return 응답 정보
     */
    public List<AreaServiceResponse> getAvailableAreas(Long concertId, Long performanceId) {
        return concertService.getAvailableAreas(concertId, performanceId)
                .stream()
                .map(AreaServiceResponse::from)
                .toList();
    }

    /**
     * 예약 가능 콘서트 공연 좌석 목록 조회
     * @param concertId 콘서트_ID
     * @param performanceId 공연_ID
     * @return 응답 정보
     */
    public List<SeatServiceResponse> getAvailableSeats(Long concertId, Long performanceId, Long areaId) {
        return concertService.getAvailableSeats(concertId, performanceId, areaId)
                .stream()
                .map(SeatServiceResponse::from)
                .toList();
    }

    /**
     * 좌석 배정 및 예약 요청
     * @param serviceRequest 요청 정보
     * @return 응답 정보
     */
    @Transactional
    public List<HoldSeatServiceResponse> holdSeats(HoldSeatServiceRequest serviceRequest) {
        return concertService.holdSeats(serviceRequest);
    }
}
