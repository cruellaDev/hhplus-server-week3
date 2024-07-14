package com.io.hhplus.concert.interfaces.concert.controller;

import com.io.hhplus.concert.application.concert.dto.PerformancesResponse;
import com.io.hhplus.concert.application.concert.dto.SeatsResponse;
import com.io.hhplus.concert.application.concert.facade.ConcertFacade;
import com.io.hhplus.concert.interfaces.common.dto.CommonResponse;
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
     * 예약가능날짜조회
     * @param concertId 콘서트_ID
     * @return 예약 가능 날짜 공연 목록
     */
    @GetMapping("/{concertId}/performances")
    public CommonResponse<PerformancesResponse> performances(@PathVariable("concertId") Long concertId) {
        return CommonResponse.success(concertFacade.getAvailablePerformances(concertId));
    }

    /**
     * 예약가능 좌석 조회
     * @param concertId 콘서트_ID
     * @param performanceId 공연_ID
     * @return 예약가능 좌석 목록
     */
    @GetMapping("/{concertId}/performances/{performanceId}/seats")
    public CommonResponse<SeatsResponse> seats(@PathVariable("concertId") Long concertId,
                                               @PathVariable("performanceId") Long performanceId) {
        return CommonResponse.success(concertFacade.getAvailableSeats(concertId, performanceId));
    }
}
