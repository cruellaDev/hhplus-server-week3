package com.io.hhplus.concert.interfaces.outbox.scheduler;

import com.io.hhplus.concert.domain.outbox.OutboxRepository;
import com.io.hhplus.concert.domain.outbox.model.Outbox;
import com.io.hhplus.concert.infrastructure.common.producer.MessageProducerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class OutboxScheduler {

    private final OutboxRepository outboxRepository;
    private final MessageProducerImpl kafkaProducer;

    /**
     * 결제 완료 Outbox 메시지 발행 처리 - 매 5분마다 실행
     */
    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    public void producePaidSuccessMessage() {
        List<Outbox> outboxes = outboxRepository.findNotPublishedPaidSuccessOutboxes();
        outboxes.forEach(outbox -> kafkaProducer.produce("PAID_SUCCESS", outbox.key(), outbox.toString()));
    }
}
