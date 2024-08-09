package com.io.hhplus.concert.domain.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentCommand {

    /**
     * 결제 완료 Command
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PayCommand {
        private UUID token;
        private Long customerId;
        private Long reservationId;
        private BigDecimal payAmount;
    }
}
