package com.io.hhplus.concert.presentation.reservation.dto.response;

import java.math.BigDecimal;
import java.util.Date;

public record PostTicketResponseBody(
        Long concertId,
        String concertName,
        Long performanceId,
        Date performedAt,
        BigDecimal price,
        String status,
        Long seatId,
        String seatNo
) {
}
