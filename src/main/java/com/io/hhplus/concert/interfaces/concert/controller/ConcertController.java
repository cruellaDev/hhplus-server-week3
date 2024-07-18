package com.io.hhplus.concert.interfaces.concert.controller;

import com.io.hhplus.concert.application.concert.dto.ConcertInfoWithPerformance;
import com.io.hhplus.concert.application.concert.dto.ConcertInfoWithPerformanceAndSeats;
import com.io.hhplus.concert.application.concert.dto.ConcertsInfo;
import com.io.hhplus.concert.application.concert.facade.ConcertFacade;
import com.io.hhplus.concert.common.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("concerts")
public class ConcertController {

    private final ConcertFacade concertFacade;

    /**
     * 예약가능 콘서트 조회
     * @return 예약 가능 콘서트 목록
     */
    @GetMapping("/")
    public CommonResponse<ConcertsInfo> concerts() {
        return CommonResponse.success(concertFacade.getAvailableConcerts());
    }

    /**
     * 예약가능날짜조회
     * @param concertId 콘서트_ID
     * @return 예약 가능 날짜 공연 목록
     */
    @GetMapping("/{concertId}/performances")
    public CommonResponse<ConcertInfoWithPerformance> performances(@PathVariable("concertId") Long concertId) {
        return CommonResponse.success(concertFacade.getAvailablePerformances(concertId));
    }

    /**
     * 예약가능 좌석 조회
     * @param concertId 콘서트_ID
     * @param performanceId 공연_ID
     * @return 예약가능 좌석 목록
     */
    @GetMapping("/{concertId}/performances/{performanceId}/seats")
    public CommonResponse<ConcertInfoWithPerformanceAndSeats> seats(@PathVariable("concertId") Long concertId,
                                                                    @PathVariable("performanceId") Long performanceId) {
        return CommonResponse.success(concertFacade.getAvailableSeats(concertId, performanceId));
    }
}
