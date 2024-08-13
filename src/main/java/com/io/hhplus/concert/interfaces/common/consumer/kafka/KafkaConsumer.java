package com.io.hhplus.concert.interfaces.common.consumer.kafka;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Data
@Service
public class KafkaConsumer {

    private CountDownLatch latch = new CountDownLatch(10);
    private List<String> messages = new ArrayList<>();

    @KafkaListener(topics = "test_topic", groupId = "group_id")
    public void consume(String message) {
        log.info("consumed message : {}", message);
        messages.add(message);
        latch.countDown();
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }

}
