package com.io.hhplus.concert.application.concert.facade;

import com.io.hhplus.concert.application.concert.dto.AvailableSeatServiceResponse;
import com.io.hhplus.concert.application.concert.dto.ConcertServiceResponse;
import com.io.hhplus.concert.application.concert.dto.AvailablePerformanceServiceResponse;
import com.io.hhplus.concert.domain.concert.service.ConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
    public List<AvailablePerformanceServiceResponse> getAvailablePerformances(Long concertId) {
        return concertService.getAvailablePerformances(concertId)
                .stream()
                .map(AvailablePerformanceServiceResponse::from)
                .toList();
    }

    /**
     * 예약 가능 콘서트 공연 좌석 목록 조회
     * @param concertId 콘서트_ID
     * @param performanceId 공연_ID
     * @return 응답 정보
     */
    public List<AvailableSeatServiceResponse> getAvailableSeats(Long concertId, Long performanceId) {
        return concertService.getAvailableSeats(concertId, performanceId)
                .stream()
                .map(AvailableSeatServiceResponse::from)
                .toList();
    }
}
