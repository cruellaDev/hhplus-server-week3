package com.io.hhplus.concert.domain.common.producer;

public interface MessageProducer {
    void produce(String topic, String key, String payload);
}
