package com.io.hhplus.concert.domain.payment.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentEvent {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaidSuccess {
        private UUID token;
        private Long customerId;
        private Long reservationId;
        private BigDecimal payAmount;
    }
}
