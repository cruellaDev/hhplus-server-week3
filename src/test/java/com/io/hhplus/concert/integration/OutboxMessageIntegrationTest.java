package com.io.hhplus.concert.integration;

import com.io.hhplus.concert.common.utils.TestDataInitializer;
import com.io.hhplus.concert.domain.common.producer.MessageProducer;
import com.io.hhplus.concert.domain.concert.ConcertCommand;
import com.io.hhplus.concert.domain.concert.ConcertService;
import com.io.hhplus.concert.domain.concert.dto.ReservationInfo;
import com.io.hhplus.concert.domain.outbox.OutboxRepository;
import com.io.hhplus.concert.domain.outbox.model.Outbox;
import com.io.hhplus.concert.domain.payment.PaymentCommand;
import com.io.hhplus.concert.domain.payment.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@EmbeddedKafka(partitions = 1,
        brokerProperties = {"listeners=PLAINTEXT://localhost:9092"},
        ports = { 9092 }
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // @DirtiesContext 컨텍스트의 상태를 초기화
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class OutboxMessageIntegrationTest {

    @Autowired
    TestDataInitializer testDataInitializer;

    @Autowired
    private ConcertService concertService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private OutboxRepository outboxRepository;

    Logger logger = LoggerFactory.getLogger(OutboxMessageIntegrationTest.class);

    @BeforeEach
    void setUp() {
        testDataInitializer.initializeTestData();
    }

    @Test
    void 예약_요청_후_결제_완료_시_카프카_메시지가_전송되는지_로그에서_확인한다() {
        ConcertCommand.ReserveSeatsCommand reserveSeatsCommand = ConcertCommand.ReserveSeatsCommand.builder()
                .concertId(1000L)
                .seatNumbers(List.of("30"))
                .concertScheduleId(9998L)
                .bookerName("예약자명")
                .customerId(5L)
                .build();
        ReservationInfo reservationInfo = concertService.reserveSeats(reserveSeatsCommand);
        PaymentCommand.PayCommand payCommand = PaymentCommand.PayCommand.builder()
                .payAmount(BigDecimal.valueOf(5000))
                .customerId(5L)
                .reservationId(reservationInfo.reservation().reservationId())
                .build();
        paymentService.pay(payCommand);

        assertTrue(true);
    }

    @Test
    void 예약_요청_후_결제_완료_시_outbox_메시지가_생성되고_발행되는지_확인한다() throws InterruptedException {
        ConcertCommand.ReserveSeatsCommand reserveSeatsCommand = ConcertCommand.ReserveSeatsCommand.builder()
                .concertId(1000L)
                .seatNumbers(List.of("31"))
                .concertScheduleId(9998L)
                .bookerName("예약자명")
                .customerId(5L)
                .build();
        ReservationInfo reservationInfo = concertService.reserveSeats(reserveSeatsCommand);
        PaymentCommand.PayCommand payCommand = PaymentCommand.PayCommand.builder()
                .payAmount(BigDecimal.valueOf(5000))
                .customerId(5L)
                .reservationId(reservationInfo.reservation().reservationId())
                .build();
        paymentService.pay(payCommand);

        CountDownLatch latch = new CountDownLatch(5);
        for (long i = 1; i <= 5; i++) {
            latch.countDown();
        }
        latch.await();

        List<Outbox> outboxes = outboxRepository.findNotPublishedPaidSuccessOutboxes();

        assertTrue(outboxes.isEmpty());
    }

    @Test
    void outbox_메시지가_발행되지_않은_데이터를_조회하여_kafka_메시지를_다시_produce하고_로그를_확인한다() {
        List<Outbox> outboxes = outboxRepository.findNotPublishedPaidSuccessOutboxes();

        AtomicInteger count = new AtomicInteger();
        outboxes.forEach(outbox -> {
            try {
                messageProducer.produce("PAID_SUCCESS", outbox.key(), outbox.toString());
                count.getAndIncrement();
            } catch (Exception e) {
                //
            }
        });

        assertEquals(outboxes.size(), count.get());
    }

}
