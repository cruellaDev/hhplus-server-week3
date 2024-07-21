package com.io.hhplus.concert.application.concert.dto;

import com.io.hhplus.concert.domain.concert.service.model.ConcertModel;
import com.io.hhplus.concert.domain.concert.service.model.PerformanceModel;
import lombok.*;

import java.util.List;

@Getter
@Builder
public class ConcertInfoWithPerformance {
    private final ConcertModel concert;
    private final List<PerformanceModel> performances;

    /**
     * @param concertModel 콘서트
     * @param performanceModels 공연목록
     */
    public ConcertInfoWithPerformance(ConcertModel concertModel, List<PerformanceModel> performanceModels) {
        this.concert = concertModel;
        this.performances = performanceModels;
    }
}
