package com.io.hhplus.concert.domain.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class PaymentCommand {

    /**
     * 결제 완료 Command
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CompletePaymentCommand {
        private Long customerId;
        private Long reservationId;
        private BigDecimal payAmount;
    }
}
