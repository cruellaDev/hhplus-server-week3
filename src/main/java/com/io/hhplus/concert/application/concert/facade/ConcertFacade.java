package com.io.hhplus.concert.application.concert.facade;

import com.io.hhplus.concert.application.concert.dto.ConcertsInfo;
import com.io.hhplus.concert.application.concert.dto.ConcertInfoWithPerformance;
import com.io.hhplus.concert.application.concert.dto.ConcertInfoWithPerformanceAndSeats;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.common.utils.DateUtils;
import com.io.hhplus.concert.domain.concert.service.ConcertValidator;
import com.io.hhplus.concert.domain.concert.service.model.ConcertModel;
import com.io.hhplus.concert.domain.concert.service.model.PerformanceModel;
import com.io.hhplus.concert.domain.concert.service.model.SeatModel;
import com.io.hhplus.concert.domain.concert.service.ConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertFacade {

    private final ConcertService concertService;
    private final ConcertValidator concertValidator;

    /**
     * 예약 가능한 콘서트 목록 조회
     * @return 콘서트 목록
     */
    public ConcertsInfo getAvailableConcerts() {
        List<ConcertModel> concertModels;
        concertModels = concertService.getAvailableConcerts();
        return new ConcertsInfo(concertModels);
    }

    /**
     * 예약 가능 콘서트 공연 목록 조회
     * @param concertId 콘서트_ID
     * @return 응답 정보
     */
    public ConcertInfoWithPerformance getAvailablePerformances(Long concertId) {
        if (concertId == null) throw new CustomException(ResponseMessage.INVALID, "콘서트 ID가 존재하지 않습니다.");
        // 콘서트 확인
        ConcertModel concertModel;
        // 콘서트 조회
        concertModel = concertService.getAvailableConcert(concertId);
        // 콘서트 검증
        concertValidator.checkIfConcertToBeReserved(concertModel);

        // 공연 확인
        List<PerformanceModel> performanceModels;
        // 공연 조회 (없을 시 빈 리스트)
        performanceModels = concertService.getAvailablePerformances(concertId);

        // response 반환
        return new ConcertInfoWithPerformance(concertModel, performanceModels);
    }

    /**
     * 예약 가능 콘서트 공연 좌석 목록 조회
     * @param concertId 콘서트_ID
     * @param performanceId 공연_ID
     * @return 응답 정보
     */
    public ConcertInfoWithPerformanceAndSeats getAvailableSeats(Long concertId, Long performanceId) {
        if (concertId == null) throw new CustomException(ResponseMessage.INVALID, "콘서트 ID가 존재하지 않습니다.");
        if (performanceId == null) throw new CustomException(ResponseMessage.INVALID, "공연 ID가 존재하지 않습니다.");

        // 콘서트 확인
        ConcertModel concertModel;
        // 콘서트 조회
        concertModel = concertService.getAvailableConcert(concertId);
        // 콘서트 예약 가능 검증
        concertValidator.checkIfConcertToBeReserved(concertModel);

        // 공연 확인
        PerformanceModel performanceModel;
        // 공연 조회
        performanceModel = concertService.getAvailablePerformance(concertId, performanceId);
        // 공연 예약 가능 검증
        // - 현재 일시 기준 예약 가능한 공연
        concertValidator.checkIfPerformanceToBeReserved(performanceModel, DateUtils.getSysDate());

        // 좌석 확인
        List<SeatModel> seatModels;
        // 좌석 조회
        // 예약 가능한 좌석이 없을 시 빈 리스트 반환
        seatModels = concertService.getAvailableSeats(performanceId);

        // response 반환
        return new ConcertInfoWithPerformanceAndSeats(concertModel, performanceModel, seatModels);
    }
}
