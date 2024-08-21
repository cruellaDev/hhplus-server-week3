package com.io.hhplus.concert.domain.payment.event;

import com.io.hhplus.concert.common.utils.DateUtils;
import com.io.hhplus.concert.domain.outbox.model.Outbox;
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

        public Outbox toEventRecordCommand() {
            return Outbox.builder()
                    .domainType("PAYMENT")
                    .eventType("PAID_SUCCESS")
                    .key(this.reservationId.toString())
                    .payload(this.toString())
                    .createdAt(DateUtils.getSysDate())
                    .build();
        }
    }
}
