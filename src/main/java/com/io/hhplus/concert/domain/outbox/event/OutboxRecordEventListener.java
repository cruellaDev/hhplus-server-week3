package com.io.hhplus.concert.domain.outbox.event;

import com.io.hhplus.concert.domain.outbox.OutboxRepository;
import com.io.hhplus.concert.domain.payment.event.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxRecordEventListener {
    private final OutboxRepository outboxRepository;

    // 1. outbox 테이블에 이벤트 기록 (TransactionPhase.BEFORE_COMMIT)
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void paidSuccessHandler(PaymentEvent.PaidSuccess event) {
        log.info("결제완료 이벤트 생성을 기록합니다");
        outboxRepository.save(event.toEventRecordCommand());
    }
}
