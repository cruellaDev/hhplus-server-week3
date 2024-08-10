package com.io.hhplus.concert.domain.payment.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void success(PaymentEvent.PaidSuccess event) {
        applicationEventPublisher.publishEvent(event);
    }
}
