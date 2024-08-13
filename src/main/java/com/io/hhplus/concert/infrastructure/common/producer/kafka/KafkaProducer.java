package com.io.hhplus.concert.infrastructure.common.producer.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaStringTemplate;

    public void produce(String topic, String payload) {
        log.info("sending payload = {} to topic={}", payload, topic);
        kafkaStringTemplate.send(topic, payload);
    }
}
