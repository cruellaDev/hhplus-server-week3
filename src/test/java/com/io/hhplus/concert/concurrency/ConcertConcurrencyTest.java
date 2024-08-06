package com.io.hhplus.concert.concurrency;

import com.io.hhplus.concert.application.concert.ConcertFacade;
import com.io.hhplus.concert.common.utils.DataBaseCleanUp;
import com.io.hhplus.concert.common.utils.TestDataInitializer;
import com.io.hhplus.concert.domain.concert.ConcertCommand;
import com.io.hhplus.concert.domain.concert.ConcertRepository;
import com.io.hhplus.concert.domain.concert.model.*;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // @DirtiesContext 컨텍스트의 상태를 초기화
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ConcertConcurrencyTest {

    @Autowired
    TestDataInitializer testDataInitializer;

    @Autowired
    private DataBaseCleanUp dataBaseCleanUp;

    @Autowired
    private ConcertFacade concertFacade;

    @Autowired
    private ConcertRepository concertRepository;

    Logger logger = LoggerFactory.getLogger(ConcertConcurrencyTest.class);

    @BeforeEach
    void setUp() {
        testDataInitializer.initializeTestData();
    }

    @AfterEach
    void tearDown() {
        dataBaseCleanUp.execute();
    }

    /**
     * 비동기
     */
    @Test
    void 동일_유저가_동시에_하나의_좌석을_여러_번_예약_요청했을_시_처음를_제외하고_전부_실패한다()  {

        // given
        long customerId = 5;
        String bookerName = "고객5";
        long concertId = 10;
        long concertScheduleId = 100;
        long concertSeatId = 100;
        String seatNumber = "3";

        // 좌석 예약 요청 command 객체 생성
        ConcertCommand.ReserveSeatsCommand command = ConcertCommand.ReserveSeatsCommand.builder()
                .customerId(customerId)
                .bookerName(bookerName)
                .concertId(concertId)
                .concertScheduleId(concertScheduleId)
                .concertSeatId(concertSeatId)
                .seatNumbers(List.of(seatNumber))
                .build();

        // when
        // 10개의 스레드를 통해 동시에 좌석 예약 시도
        int numberOfThreads = 10;

        // 시작 시간 기록
        Instant testStart = Instant.now();
        logger.debug("테스트 시작 : {}", testStart);

        // 각 스레드에서 요청 시도
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<CompletableFuture<Exception>> futures = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            futures.add(CompletableFuture.supplyAsync(() -> {
                String currentThreadNm = Thread.currentThread().getName();
                Instant start = Instant.now();
                logger.info("{} - 개별 스레드 시작 : {}", currentThreadNm, start);
                try {
                    concertFacade.reserveSeats(command);
                    return null;
                } catch (Exception e) {
                    logger.error("{} - 개별 스레드 예외 : {}", currentThreadNm, e.getMessage());
                    return e;
                } finally {
                    Instant end = Instant.now();
                    logger.info("{} - 개별 스레드 종료 : {}", currentThreadNm, end);
                    logger.info("{} - 개별 스레드 경과 : {}", currentThreadNm, Duration.between(start, end).toMillis());
                }
            }, executorService));
        }

        // 모든 작업이 완료되기를 기다림
        List<Exception> exceptions = futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .toList();

        // 종료 시간 기록
        Instant testEnd = Instant.now();
        logger.info("테스트 종료 : {}", testEnd);
        logger.info("테스트 총 경과 시간 : {} ms", Duration.between(testStart, testEnd).toMillis());

        // then
        // 예약 성공 결과 확인 (단 하나의 예약만 성공했는지 확인)
        List<Reservation> reservations = concertRepository.findReservationsAlreadyExists(concertId, concertScheduleId, List.of(seatNumber));
        assertThat(reservations).hasSize(1);

        // 예외 발생 스레드 개수 체크 (단 하나의 스레드만 성공했는지 검증)
        int numberOfExceptions = exceptions.size();
        assertEquals(numberOfExceptions, numberOfThreads - 1, "예외 발생 스레드 개수 불일치");
    }

    /**
     * 동기
     */
    @Test
    void 동일_유저가_하나의_좌석을_순차적으로_예약_요청했을_시_하나를_제외하고_전부_실패한다()  {

        // given
        long customerId = 5;
        String bookerName = "고객5";
        long concertId = 10;
        long concertScheduleId = 100;
        long concertSeatId = 100;
        String seatNumber = "3";

        // 좌석 예약 요청 command 객체 생성
        ConcertCommand.ReserveSeatsCommand command = ConcertCommand.ReserveSeatsCommand.builder()
                .customerId(customerId)
                .bookerName(bookerName)
                .concertId(concertId)
                .concertScheduleId(concertScheduleId)
                .concertSeatId(concertSeatId)
                .seatNumbers(List.of(seatNumber))
                .build();

        // when
        // 10번 순차적으로 좌석 예약 시도
        int numberOfThreads = 10;

        // 예외 발생 개수
        int numberOfExceptions = 0;

        // 시작 시간 기록
        Instant testStart = Instant.now();
        logger.debug("테스트 시작 : {}", testStart);

        // 순차적으로 요청 시도
        for (int i = 0; i < numberOfThreads; i++) {
            String currentThreadNm = Thread.currentThread().getName();
            Instant start = Instant.now();
            logger.info("{} - 시작 : {}", currentThreadNm, start);
            try {
                concertFacade.reserveSeats(command);
            } catch (Exception e) {
                numberOfExceptions++;
                logger.error("{} - 예외 : {}", currentThreadNm, e.getMessage());
            } finally {
                Instant end = Instant.now();
                logger.info("{} - 종료 : {}", currentThreadNm, end);
                logger.info("{} - 경과 : {}", currentThreadNm, Duration.between(start, end).toMillis());
            }
        }

        // 종료 시간 기록
        Instant testEnd = Instant.now();
        logger.info("테스트 종료 : {}", testEnd);
        logger.info("테스트 총 경과 시간 : {} ms", Duration.between(testStart, testEnd).toMillis());

        // then
        // 예약 성공 결과 확인 (단 하나의 예약만 성공했는지 확인)
        List<Reservation> reservations = concertRepository.findReservationsAlreadyExists(concertId, concertScheduleId, List.of(seatNumber));
        assertThat(reservations).hasSize(1);

        // 예외 발생 개수 체크 (단 하나만 성공했는지 검증)
        assertEquals(numberOfExceptions, numberOfThreads - 1, "예외 발생 개수 불일치");
    }
}
