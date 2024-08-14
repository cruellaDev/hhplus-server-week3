package com.io.hhplus.concert.domain.outbox.event;

import com.io.hhplus.concert.domain.common.producer.MessageProducer;
import com.io.hhplus.concert.domain.payment.event.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxMessageEventListener {

    private final MessageProducer messageProducer;

    // 2. 카프카에 메시지 전송 (TransactionPhase.AFTER_COMMIT)
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void paidSuccessHandler(PaymentEvent.PaidSuccess event) {
        log.info("결제완료 이벤트 발행을 기록합니다");
        messageProducer.produce("PAID_SUCCESS", event.getReservationId().toString(), event.toEventRecordCommand().toString());
    }
}
