package com.io.hhplus.concert;

import com.io.hhplus.concert.interfaces.common.consumer.kafka.KafkaConsumer;
import com.io.hhplus.concert.infrastructure.common.producer.kafka.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1,
        brokerProperties = {"listeners=PLAINTEXT://localhost:9092"},
        ports = { 9092 }
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class KafkaConsumerTest {

    @Autowired
    private KafkaConsumer consumer;

    @Autowired
    private KafkaProducer producer;

    Logger logger = LoggerFactory.getLogger(KafkaConsumerTest.class);

    @Test
    public void 카프카_메시지_전송_테스트()
            throws Exception {

        String topic = "test_topic";
        String payload_even = "test_payload_even";
        String payload_odd = "test_payload_odd";

        int testCnt = 0;
        for (int i = 0; i < 10; i++) {
            if (testCnt % 2 == 0) {
                producer.produce(topic, payload_even);
            } else {
                producer.produce(topic, payload_odd);
            }
            testCnt++;
        };

        // 모든 메시지를 수신할 때까지 기다립니다.
        consumer.getLatch().await(10, TimeUnit.SECONDS);

        logger.info("============================================================");
        logger.info("consumed messages - size : {}, contents: {}", consumer.getMessages().size(), consumer.getMessages());
        assertThat(consumer.getMessages()).hasSize(10);
        logger.info("============================================================");

    }

}
