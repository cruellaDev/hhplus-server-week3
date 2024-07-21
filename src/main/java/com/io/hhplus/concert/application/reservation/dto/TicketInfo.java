package com.io.hhplus.concert.application.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class TicketInfo {
    private final Long concertId;
    private final Long performanceId;
    private final BigDecimal performancePrice;
    private final Date performedAt;
    private final List<Long> seatIds;
}
