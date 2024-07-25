package com.io.hhplus.concert.domain.concert.service;

import com.io.hhplus.concert.domain.concert.model.Concert;
import com.io.hhplus.concert.domain.concert.model.Performance;
import com.io.hhplus.concert.domain.concert.model.Seat;
import com.io.hhplus.concert.domain.concert.repository.ConcertRepository;
import com.io.hhplus.concert.domain.concert.repository.PerformanceRepository;
import com.io.hhplus.concert.domain.concert.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final PerformanceRepository performanceRepository;
    private final SeatRepository seatRepository;

    /**
     * 콘서트 목록 조회
     * @return 콘서트 목록
     */
    public List<Concert> getConcerts() {
        return concertRepository.findConcerts()
                .stream()
                .filter(Concert::isNotDeleted)
                .toList();
    }

    /**
     * 현재일시 기준 특정 콘서트의 예약 가능 공연 목록 조회
     * @param concertId 콘서트 ID
     * @return 현재일시 기준 특정 콘서트의 예약가능 공연 목록 조회
     */
    public List<Performance> getAvailablePerformances(Long concertId) {
        log.info("[service-concert-get-performances] - concertId : {}", concertId);
        return performanceRepository.findPerformances(concertId)
                .stream()
                .filter(performance
                        -> performance.isToBePerformed()
                        && performance.isNotDeleted())
                .toList();
    }

    /**
     * 현재일시 기준 특정 콘서트의 공연 좌석 목록 조회
     * @param performanceId 공연_ID
     * @return 예약 가능 좌석 목록
     */
    public List<Seat> getAvailableSeats(Long concertId, Long performanceId) {
        log.info("[service-concert-get-seats] - concertId : {}, performanceId : {}", concertId, performanceId);
        return seatRepository.findSeats(concertId, performanceId)
                .stream()
                .filter(seat
                        -> seat.isAvailableStatus()
                        && seat.isNotDeleted())
                .toList();
    }

    /**
     * TODO 좌석 배정
     * @param seatId 좌석_ID
     * @return 좌석 임시 배정 결과
     */
    public Object assignSeat(Long seatId) {
        return null;
    }

    /**
     * TODO 좌석 배정 취소
     * @param seatId 좌석_ID
     * @return 좌석 배정 결과
     */
    public List<Object> cancelSeatAssignment(Long seatId) {
        return List.of();
    }

    /**
     * TODO 좌석 예약 완료
     * @param seatId 좌석_ID
     * @return 좌석 배정 결과
     */
    public Object completeSeatBooking(Long seatId) {
        return null;
    }
}
