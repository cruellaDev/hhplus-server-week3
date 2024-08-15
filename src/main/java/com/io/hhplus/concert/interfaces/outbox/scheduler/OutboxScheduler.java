package com.io.hhplus.concert.interfaces.outbox.scheduler;

import com.io.hhplus.concert.domain.outbox.OutboxRepository;
import com.io.hhplus.concert.domain.outbox.model.Outbox;
import com.io.hhplus.concert.infrastructure.common.producer.MessageProducerImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxScheduler {

    private final OutboxRepository outboxRepository;
    private final MessageProducerImpl messageProducer;

    /**
     * 결제 완료 Outbox 메시지 발행 처리 - 매 5분마다 실행
     */
    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    public void producePaidSuccessMessage() {
        List<Outbox> outboxes = outboxRepository.findNotPublishedPaidSuccessOutboxes();

        if (outboxes.isEmpty()) return;

        AtomicInteger count = new AtomicInteger();
        outboxes.forEach(outbox -> {
            try {
                messageProducer.produce("PAID_SUCCESS", outbox.key(), outbox.toString());
                count.getAndIncrement();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        log.info("총 {} 건의 결제완료 메시지 중 {} 건이 발행되었습니다.", outboxes.size(), count.get());
    }
}
