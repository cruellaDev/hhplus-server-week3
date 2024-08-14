package com.io.hhplus.concert.interfaces.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.io.hhplus.concert.domain.outbox.OutboxRepository;
import com.io.hhplus.concert.domain.outbox.model.Outbox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageConsumer {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    private final String TOPIC_PAID_SUCCESS = "PAID_SUCCESS";

    @KafkaListener(topics = TOPIC_PAID_SUCCESS)
    public void consume(String key, String message) throws JsonProcessingException {
        Outbox payload = objectMapper.readValue(message, Outbox.class);
        Optional<Outbox> optionalOutbox = outboxRepository.findPaidSuccessOutbox(key);
        if (optionalOutbox.isEmpty()) {
            log.warn("[KAFKA] outbox is not found - topic : {}, payload : {}", TOPIC_PAID_SUCCESS, payload);
            return;
        }
        Outbox outbox = optionalOutbox.get().publish();
        outboxRepository.save(outbox);
        log.info("[KAFKA] consumed message - topic : {}, outbox : {}", TOPIC_PAID_SUCCESS, outbox);
    }
}
