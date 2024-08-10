package com.io.hhplus.concert.domain.customer.event;

import com.io.hhplus.concert.domain.customer.CustomerCommand;
import com.io.hhplus.concert.domain.customer.CustomerService;
import com.io.hhplus.concert.domain.payment.event.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerPointEventListener {

    private final CustomerService customerService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void paidSuccessHandler(PaymentEvent.PaidSuccess event) {
        log.info("결제 완료 - 포인트를 차감합니다.");
        CustomerCommand.UseCustomerPointCommand command = CustomerCommand.UseCustomerPointCommand.builder()
                .customerId(event.getCustomerId())
                .amount(event.getPayAmount())
                .build();
        customerService.useCustomerPoint(command);
    }

}
