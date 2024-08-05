package com.io.hhplus.concert.domain.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class CustomerCommand {

    /**
     * 고객 등록 Command
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RegisterCustomerCommand {
        private String customerName;
    }

    /**
     * 고객 포인트 충전 Command
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChargeCustomerPointCommand {
        private Long customerId;
        private BigDecimal amount;
    }

    /**
     * 고객 포인트 사용 Command
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UseCustomerPointCommand {
        private Long customerId;
        private BigDecimal amount;
    }
}
