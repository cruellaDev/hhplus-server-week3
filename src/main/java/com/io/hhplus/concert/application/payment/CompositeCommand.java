package com.io.hhplus.concert.application.payment;

import com.io.hhplus.concert.domain.concert.ConcertCommand;
import com.io.hhplus.concert.domain.customer.CustomerCommand;
import com.io.hhplus.concert.domain.payment.PaymentCommand;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
public class CompositeCommand {
    /**
     * 결제 요청 Command
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CheckoutPaymentCommand {
        private Long customerId;
        private Long reservationId;
        private BigDecimal payAmount;

        public CustomerCommand.UseCustomerPointCommand toUseCustomerPointCommand() {
            return CustomerCommand.UseCustomerPointCommand.builder()
                    .customerId(this.customerId)
                    .amount(this.payAmount)
                    .build();
        }

        public ConcertCommand.ConfirmReservationCommand toConfirmReservationCommand() {
            return ConcertCommand.ConfirmReservationCommand.builder()
                    .customerId(this.customerId)
                    .reservationId(this.reservationId)
                    .build();
        }

        public PaymentCommand.CompletePaymentCommand toCompletePaymentCommand() {
            return PaymentCommand.CompletePaymentCommand.builder()
                    .customerId(this.customerId)
                    .reservationId(this.reservationId)
                    .payAmount(this.payAmount)
                    .build();
        }
    }
}
