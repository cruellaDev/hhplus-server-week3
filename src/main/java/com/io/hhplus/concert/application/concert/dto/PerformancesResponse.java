package com.io.hhplus.concert.application.concert.dto;

import com.io.hhplus.concert.domain.concert.model.Concert;
import com.io.hhplus.concert.domain.concert.model.Performance;
import lombok.*;

import java.util.List;

@Getter
public class PerformancesResponse extends ConcertResponse {
    private final List<Performance> performances;

    /**
     * @param concert 콘서트
     * @param performances 공연목록
     */
    public PerformancesResponse(Concert concert, List<Performance> performances) {
        super(concert);
        this.performances = performances;
    }
}
