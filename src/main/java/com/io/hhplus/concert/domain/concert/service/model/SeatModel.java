package com.io.hhplus.concert.domain.concert.service.model;

import com.io.hhplus.concert.common.enums.SeatStatus;

public record SeatModel(
        Long seatId,
        Long performanceId,
        Long concertId,
        String seatNo,
        SeatStatus seatStatus
) {
    public static SeatModel create(Long seatId, Long performanceId, Long concertId, String seatNo, SeatStatus seatStatus) {
        return new SeatModel(seatId, performanceId, concertId, seatNo, seatStatus);
    }

    public static SeatModel noContents() {
        return create(-1L, -1L, -1L, "N/A", SeatStatus.NOT_AVAILABLE);
    }

}
