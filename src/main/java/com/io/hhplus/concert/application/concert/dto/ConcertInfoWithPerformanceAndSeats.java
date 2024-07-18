package com.io.hhplus.concert.application.concert.dto;

import com.io.hhplus.concert.domain.concert.service.model.ConcertModel;
import com.io.hhplus.concert.domain.concert.service.model.PerformanceModel;
import com.io.hhplus.concert.domain.concert.service.model.SeatModel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ConcertInfoWithPerformanceAndSeats {
    private final ConcertModel concert;
    private final PerformanceModel performance;
    private final List<SeatModel> seats;

    /**
     * @param concertModel 콘서트
     * @param performanceModel 공연
     * @param seatModels 좌석목록
     */
    public ConcertInfoWithPerformanceAndSeats(ConcertModel concertModel, PerformanceModel performanceModel, List<SeatModel> seatModels) {
        this.concert = concertModel;
        this.performance = performanceModel;
        this.seats = seatModels;
    }
}
