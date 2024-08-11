package com.io.hhplus.concert.interfaces.concert.controller;

import com.io.hhplus.concert.common.dto.CommonResponse;
import com.io.hhplus.concert.domain.concert.ConcertService;
import com.io.hhplus.concert.interfaces.concert.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("concerts")
public class ConcertController {

    private final ConcertService concertService;

    /**
     * 콘서트 등록
     * @param request 콘서트 등록 요청 정보
     * @return 콘서트 등록 응답 정보
     */
    @PostMapping("/register/concert")
    public ResponseEntity<CommonResponse<ConcertDto.SingleResponse>> register(@RequestBody ConcertDto.RegisterRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(ConcertDto.SingleResponse.from(concertService.registerConcert(request.toCommand()))));
    }

    /**
     * 콘서트 일정 등록
     * @param request 콘서트 일정 등록 요청 정보
     * @return 콘서트 일정 등록 응답 정보
     */
    @PostMapping("/register/schedule")
    public ResponseEntity<CommonResponse<ConcertScheduleDto.SingleResponse>> register(@RequestBody ConcertScheduleDto.RegisterRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(ConcertScheduleDto.SingleResponse.from(concertService.registerConcertSchedule(request.toCommand()))));
    }

    /**
     * 콘서트 좌석 등록
     * @param request 콘서트 좌석 등록 요청 정보
     * @return 콘서트 좌석 등록 응답 정보
     */
    @PostMapping("/register/seat")
    public ResponseEntity<CommonResponse<ConcertSeatDto.SingleResponse>> register(@RequestBody ConcertSeatDto.RegisterRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(ConcertSeatDto.SingleResponse.from(concertService.registerConcertSeat(request.toCommand()))));
    }

    /**
     * 예약가능 콘서트 조회
     * @return 예약 가능 콘서트 목록
     */
    @GetMapping("/")
    public ResponseEntity<CommonResponse<ConcertDto.ListResponse>> concerts() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(ConcertDto.ListResponse.from(concertService.getAvailableConcerts())));
    }

    /**
     * 예약가능날짜조회
     * @param concertId 콘서트_ID
     * @return 예약 가능 날짜 공연 목록
     */
    @GetMapping("/{concertId}/schedules")
    public ResponseEntity<CommonResponse<ConcertScheduleDto.ListResponse>> schedules(@PathVariable("concertId") Long concertId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(ConcertScheduleDto.ListResponse.from(concertService.getAvailableSchedules(concertId))));
    }

    /**
     * 예약가능 좌석 목록 조회
     * @param concertId 콘서트_ID
     * @param concertScheduleId 공연_일정_ID
     * @return 예약가능 좌석 목록
     */
    @GetMapping("/{concertId}/schedules/{concertScheduleId}/seats")
    public ResponseEntity<CommonResponse<ConcertSeatDto.ListResponse>> seats(@PathVariable("concertId") Long concertId,
                                                         @PathVariable("concertScheduleId") Long concertScheduleId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(ConcertSeatDto.ListResponse.from(concertService.getAvailableSeats(concertId, concertScheduleId))));
    }

    /**
     * 좌석 배정 및 예약 요청
     * @param request 요청 정보
     * @return 응답 정보
     */
    @PostMapping("/reserve/seats")
    public ResponseEntity<CommonResponse<ReservationDto.ReserveSeatsResponse>> reserveSeats(@RequestBody ReservationDto.ReserveSeatsRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(ReservationDto.ReserveSeatsResponse.from(concertService.reserveSeats(request.toCommand()))));
    }
}
