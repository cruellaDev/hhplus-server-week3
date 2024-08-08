package com.io.hhplus.concert.application.concert;

import com.io.hhplus.concert.domain.concert.ConcertCommand;
import com.io.hhplus.concert.domain.concert.ConcertService;
import com.io.hhplus.concert.domain.concert.dto.AvailableSeatInfo;
import com.io.hhplus.concert.domain.concert.dto.ReservationInfo;
import com.io.hhplus.concert.domain.concert.model.Concert;
import com.io.hhplus.concert.domain.concert.model.ConcertSchedule;
import com.io.hhplus.concert.domain.concert.model.ConcertSeat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertFacade {

    private final ConcertService concertService;

    /**
     * 콘서트 등록
     * @param command 콘서트 등록 command
     * @return 콘서트 저장 정보
     */
    public Concert registerConcert(ConcertCommand.RegisterConcertCommand command) {
        return concertService.registerConcert(command);
    }

    /**
     * 콘서트 일정 등록
     * @param command 콘서트 일정 등록 command
     * @return 콘서트 일정 저장 정보
     */
    public ConcertSchedule registerConcertSchedule(ConcertCommand.RegisterConcertScheduleCommand command) {
        return concertService.registerConcertSchedule(command);
    }

    /**
     * 콘서트 좌석 등록
     * @param command 콘서트 좌석 등록 command
     * @return 콘서트 좌석 저장 정보
     */
    public ConcertSeat registerConcertSeat(ConcertCommand.RegisterConcertSeatCommand command) {
        return concertService.registerConcertSeat(command);
    }

    /**
     * 예약 가능한 콘서트 목록 조회
     * @return 콘서트 목록
     */
    public List<Concert> getAvailableConcerts() {
        return concertService.getAvailableConcerts();
    }

    /**
     * 예약 가능 콘서트 공연 목록 조회
     * @param concertId 콘서트_ID
     * @return 응답 정보
     */
    public List<ConcertSchedule> getAvailableSchedules(Long concertId) {
        return concertService.getAvailableSchedules(concertId);
    }

    /**
     * 예약 가능 콘서트 공연 좌석 목록 조회
     * @param concertId 콘서트_ID
     * @param concertScheduleId 공연_ID
     * @return 응답 정보
     */
    public List<AvailableSeatInfo> getAvailableSeats(Long concertId, Long concertScheduleId) {
        return concertService.getAvailableSeats(concertId, concertScheduleId);
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
