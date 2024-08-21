package com.io.hhplus.concert.infrastructure.common.producer;

import com.io.hhplus.concert.domain.common.producer.MessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageProducerImpl implements MessageProducer {

    private final KafkaTemplate<String, String> kafkaStringTemplate;

    public void produce(String topic, String key, String payload) {
        log.info("[KAFKA] producing message to topic : {} with key : {} and payload : {}", topic, key, payload);
        kafkaStringTemplate.send(topic, key, payload);
    }
}
