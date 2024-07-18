package com.io.hhplus.concert.domain.concert.service;

import com.io.hhplus.concert.common.enums.SeatStatus;
import com.io.hhplus.concert.domain.concert.service.model.ConcertModel;
import com.io.hhplus.concert.domain.concert.service.model.PerformanceModel;
import com.io.hhplus.concert.domain.concert.service.model.SeatModel;
import com.io.hhplus.concert.domain.concert.repository.ConcertRepository;
import com.io.hhplus.concert.domain.concert.repository.PerformanceRepository;
import com.io.hhplus.concert.domain.concert.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final PerformanceRepository performanceRepository;
    private final SeatRepository seatRepository;

    /**
     * 예약 가능 콘서트 목록 조회
     * @return 콘서트 목록
     */
    public List<ConcertModel> getAvailableConcerts() {
        return concertRepository.findAvailableAll();
    }

    /**
     * 예약 가능 콘서트 단건 조회
     * @param concertId 콘서트_ID
     * @return 콘서트 정보
     */
    public ConcertModel getAvailableConcert(Long concertId) {
        log.info("[service-concert-getAvailableConcert]: id: {}", concertId);
        return concertRepository.findAvailableOneById(concertId).orElseGet(ConcertModel::noContents);
    }

    /**
     * 예약 가능 콘서트 공연 목록 조회
     */
    public List<PerformanceModel> getAvailablePerformances(Long concertId) {
        return performanceRepository.findAvailableAllByConcertId(concertId);
    }

    /**
     * 예약 가능 콘서트 공연 단건 조회
     */
    public PerformanceModel getAvailablePerformance(Long concertId, Long performanceId) {
        log.info("[service-concert-getAvailablePerformance]: id: {}", performanceId);
        return performanceRepository.findAvailableOneByConcertIdAndPerformanceId(concertId, performanceId)
                .orElseGet(PerformanceModel::noContents);
    }

    /**
     * 예약 가능 콘서트 공연 좌석 목록 조회
     * @param performanceId 공연_ID
     * @return 예약 가능 좌석 목록
     */
    public List<SeatModel> getAvailableSeats(Long performanceId) {
        return seatRepository.findAvailableAllByPerformanceId(performanceId);
    }

    /**
     * 예약 가능 콘서트 공연 좌석 단건 조회
     * @param performanceId 공연_ID
     * @param seatId 좌석_ID
     * @return 예약 가능 좌석 정보
     */
    public SeatModel getAvailableSeat(Long performanceId, Long seatId) {
        log.info("[service-concert-getAvailableSeat]: id: {}", seatId);
        return seatRepository.findAvailableOneByPerformanceIdAndSeatId(performanceId, seatId).orElseGet(SeatModel::noContents);
    }

    /**
     * 좌석 임시 배정
     * @param seatId 좌석_ID
     * @return 좌석 임시 배정 결과
     */
    public SeatModel assignSeatTemporarily(Long seatId) {
        SeatModel asisSeatModel = seatRepository.findById(seatId).orElseGet(SeatModel::noContents);
        SeatModel tobeSeatModel = SeatModel.create(asisSeatModel.seatId(), asisSeatModel.performanceId(), asisSeatModel.concertId(), asisSeatModel.seatNo(), SeatStatus.WAITING_FOR_RESERVATION);
        return seatRepository.save(tobeSeatModel);
    }

    /**
     * 좌석 임시 배정 상태 변경
     * @param reservationId 예약_ID
     * @param asisSeatStatus 기존 좌석_상태
     * @param tobeSeatStatus 이후 좌석_상태
     */
    public List<SeatModel> updateSeatStatusByReservationIdAndReservationStatus(Long reservationId, SeatStatus asisSeatStatus, SeatStatus tobeSeatStatus) {
        List<SeatModel> seatModels = seatRepository.findAllByReservationIdAndSeatStatus(reservationId, asisSeatStatus);
        return seatModels.stream()
                .map(asisSeatModel -> {
                    SeatModel tobeSeatModel = SeatModel.create(asisSeatModel.seatId(), asisSeatModel.performanceId(), asisSeatModel.concertId(), asisSeatModel.seatNo(), tobeSeatStatus);
                    return seatRepository.save(tobeSeatModel);
                })
                .collect(Collectors.toList());
    }
}
