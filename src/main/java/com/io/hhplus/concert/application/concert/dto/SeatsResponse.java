package com.io.hhplus.concert.application.concert.dto;

import com.io.hhplus.concert.domain.concert.model.Concert;
import com.io.hhplus.concert.domain.concert.model.Performance;
import com.io.hhplus.concert.domain.concert.model.Seat;
import lombok.Getter;

import java.util.List;

@Getter
public class SeatsResponse extends ConcertResponse {
    private final Performance performance;
    private final List<Seat> seats;

    /**
     * @param concert 콘서트
     * @param performance 공연
     * @param seats 좌석목록
     */
    public SeatsResponse(Concert concert, Performance performance, List<Seat> seats) {
        super(concert);
        this.performance = performance;
        this.seats = seats;
    }
}
