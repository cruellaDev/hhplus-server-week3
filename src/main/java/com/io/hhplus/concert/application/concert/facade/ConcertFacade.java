package com.io.hhplus.concert.application.concert.facade;

import com.io.hhplus.concert.application.concert.dto.*;
import com.io.hhplus.concert.domain.concert.ConcertCommand;
import com.io.hhplus.concert.domain.concert.ConcertService;
import com.io.hhplus.concert.domain.concert.dto.ReservationInfo;
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
    public List<PerformanceServiceResponse> getAvailableSchedules(Long concertId) {
        return concertService.getAvailableSchedules(concertId)
                .stream()
                .map(PerformanceServiceResponse::from)
                .toList();
    }

    /**
     * 예약 가능 콘서트 공연 좌석 목록 조회
     * @param concertId 콘서트_ID
     * @param concertScheduleId 공연_ID
     * @return 응답 정보
     */
    public List<SeatServiceResponse> getAvailableSeats(Long concertId, Long concertScheduleId) {
        return concertService.getAvailableSeats(concertId, concertScheduleId)
                .stream()
                .map(SeatServiceResponse::from)
                .toList();
    }

    /**
     * 좌석 배정 및 예약 요청
     * @param command 좌석 예약 command
     * @return 응답 정보
     */
    @Transactional
    public ReservationInfo reserveSeats(ConcertCommand.ReserveSeatsCommand command) {
        return concertService.reserveSeats(command);
    }
}
