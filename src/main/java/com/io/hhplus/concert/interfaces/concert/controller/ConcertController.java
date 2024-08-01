package com.io.hhplus.concert.interfaces.concert.controller;

import com.io.hhplus.concert.application.concert.facade.ConcertFacade;
import com.io.hhplus.concert.common.dto.CommonResponse;
import com.io.hhplus.concert.interfaces.concert.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


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
    public CommonResponse<ConcertDto.Response> concerts() {
        return CommonResponse.success(ConcertDto.Response.from(concertFacade.getAvailableConcerts()));
    }

    /**
     * 예약가능날짜조회
     * @param concertId 콘서트_ID
     * @return 예약 가능 날짜 공연 목록
     */
    @GetMapping("/{concertId}/schedules")
    public CommonResponse<PerformanceDto.Response> performances(@PathVariable("concertId") Long concertId) {
        return CommonResponse.success(PerformanceDto.Response.from(concertFacade.getAvailableSchedules(concertId)));
    }

    /**
     * 예약가능 좌석 목록 조회
     * @param concertId 콘서트_ID
     * @param concertScheduleId 공연_일정_ID
     * @return 예약가능 좌석 목록
     */
    @GetMapping("/{concertId}/schedules/{concertScheduleId}/seats/{concertSeatId}")
    public CommonResponse<SeatDto.Response> seats(@PathVariable("concertId") Long concertId,
                                                  @PathVariable("concertScheduleId") Long concertScheduleId) {
        return CommonResponse.success(SeatDto.Response.from(concertFacade.getAvailableSeats(concertId, concertScheduleId)));
    }

    /**
     * 좌석 배정 및 예약 요청
     * @param request 요청 정보
     * @return 응답 정보
     */
    @PostMapping("/reserve/seats")
    public CommonResponse<ReservationDto.ReserveSeatsResponse> reserveSeats(ReservationDto.ReserveSeatsRequest request) {
        return CommonResponse.success(ReservationDto.ReserveSeatsResponse.from(concertFacade.reserveSeats(request.toCommand())));
    }
}
