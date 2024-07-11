package com.io.hhplus.concert.domain.concert.service;

import com.io.hhplus.concert.domain.concert.model.Performance;
import com.io.hhplus.concert.domain.concert.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;

    /**
     * 예약 가능 콘서트 공연 목록 조회
     */
    public List<Performance> getAvailablePerformances(Long concertId) {
        return performanceRepository.findAvailableAllByConcertId(concertId);
    }

    /**
     * 예약 가능 콘서트 공연 단건 조회
     */
    public Performance getAvailablePerformance(Long concertId, Long performanceId) {
        return performanceRepository.findAvailableOneByConcertIdAndPerformanceId(concertId, performanceId)
                .orElseGet(Performance::noContents);
    }

    /**
     * 콘서트 공연 예약 가능 검증
     * @param performance 콘서트
     * @return 예약 가능 여부
     */
    public boolean meetsIfPerformanceToBeReserved(Performance performance) {
        // 예약 가능 일시는 현재 일시를 기준으로 한다.
        Date currentDate = new Date();
        return performance != null
                && Performance.isAvailablePerformanceId(performance.performanceId())
                && Performance.isAvailableCapacityLimit(performance.capacityLimit())
                && Performance.isPerformAtAfterTargetDate(performance.performedAt(), currentDate)
                ;
    }

}
