package com.io.hhplus.concert.application.payment.facade;

import com.io.hhplus.concert.application.payment.PaymentFacade;
import com.io.hhplus.concert.domain.queue.QueueTokenRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReservationIntegrationTest {

    @Autowired
    private PaymentFacade paymentFacade;

    @Autowired
    private QueueTokenRepository waitingRepository;

    @BeforeEach
    void setUp() {
        for (long i = 2; i < 10; i++) {
//            WaitingQueueModel waitingQueueModel = waitingRepository.saveWaitingQueue(WaitingQueueModel.create(null, i, "userToken" + (new Date()).toString(), WaitingStatus.ACTIVE, null, null));
//            waitingRepository.saveWaitingEnterHistory(WaitingEnterHistoryModel.create(null, waitingQueueModel.waitingId(), null));
        }

        for (long i = 4; i < 50; i+=5) {
//            seatRepository.save(SeatModel.create(i, 1L, 1L, String.valueOf(i), SeatStatus.WAITING_FOR_RESERVATION));
        }
    }

    @AfterEach
    void tearDown() {
        waitingRepository.deleteAllWaitingQueue();
        waitingRepository.deleteAllWaitingEnterHistory();

        for (long i = 4; i < 50; i+=5) {
//            seatRepository.save(SeatModel.create(i, 1L, 1L, String.valueOf(i), SeatStatus.AVAILABLE));
        }
    }

    /**
     * 유효하지 않은 고객의 예약 요청
     */
//    @Test
//    void 예약_요청_유효하지_않은_고객일_시_예외_처리() {
//        // given
//        long customerId = 0;
//        ReserverInfo reserverInfo = new ReserverInfo(
//                "테스트사용자",
//                ReceiveMethod.ONLINE,
//                "테스트수령인",
//                null,
//                null,
//                null
//        );
//        TicketInfo ticketInfo = new TicketInfo(
//                1L,
//                1L,
//                BigDecimal.valueOf(10000),
//                DateUtils.createTemporalDateByIntParameters(2024, 8, 12, 23,0,0),
//                List.of(3L)
//        );
//
//        // when - then
//        assertThatThrownBy(() -> reservationFacade.requestReservation(customerId, reserverInfo, ticketInfo))
//                .isInstanceOf(CustomException.class)
//                .extracting("responseMessage")
//                .isEqualTo(ResponseMessage.NOT_AVAILABLE);
//
//    }
//
//    /**
//     * 대기열에 없는 고객의 예약 요청
//     */
//    @Test
//    void 예약_요청_대기열에_없는_고객일_시_예외_처리() {
//        // given
//        long customerId = 1;
//        ReserverInfo reserverInfo = new ReserverInfo(
//                "테스트사용자",
//                ReceiveMethod.ONLINE,
//                "테스트수령인",
//                null,
//                null,
//                null
//        );
//        TicketInfo ticketInfo = new TicketInfo(
//                1L,
//                1L,
//                BigDecimal.valueOf(10000),
//                DateUtils.createTemporalDateByIntParameters(2024, 8, 12, 23,0,0),
//                List.of(3L)
//        );
//
//        // when - then
//        assertThatThrownBy(() -> reservationFacade.requestReservation(customerId, reserverInfo, ticketInfo))
//                .isInstanceOf(CustomException.class)
//                .extracting("responseMessage")
//                .isEqualTo(ResponseMessage.WAITING_NOT_FOUND);
//
//    }
//
//    /**
//     * 유효하지 않은 콘서트에 대한 고객의 예약 요청
//     */
//    @Test
//    void 예약_요청_유효하지_않은_콘서트일_시_예외_처리() {
//        // given
//        long customerId = 2;
//        ReserverInfo reserverInfo = new ReserverInfo(
//                "테스트사용자",
//                ReceiveMethod.ONLINE,
//                "테스트수령인",
//                null,
//                null,
//                null
//        );
//        TicketInfo ticketInfo = new TicketInfo(
//                999L,
//                1L,
//                BigDecimal.valueOf(10000),
//                DateUtils.createTemporalDateByIntParameters(2024, 8, 12, 23,0,0),
//                List.of(3L)
//        );
//
//        // when - then
//        assertThatThrownBy(() -> reservationFacade.requestReservation(customerId, reserverInfo, ticketInfo))
//                .isInstanceOf(CustomException.class)
//                .extracting("responseMessage")
//                .isEqualTo(ResponseMessage.NOT_AVAILABLE);
//
//    }
//
//    /**
//     * 유효하지 않은 공연에 대한 고객의 예약 요청
//     */
//    @Test
//    void 예약_요청_유효하지_않은_공연일_시_예외_처리() {
//        // given
//        long customerId = 2;
//        ReserverInfo reserverInfo = new ReserverInfo(
//                "테스트사용자",
//                ReceiveMethod.ONLINE,
//                "테스트수령인",
//                null,
//                null,
//                null
//        );
//        TicketInfo ticketInfo = new TicketInfo(
//                1L,
//                999L,
//                BigDecimal.valueOf(10000),
//                DateUtils.createTemporalDateByIntParameters(2024, 8, 12, 23,0,0),
//                List.of(3L)
//        );
//
//        // when - then
//        assertThatThrownBy(() -> reservationFacade.requestReservation(customerId, reserverInfo, ticketInfo))
//                .isInstanceOf(CustomException.class)
//                .extracting("responseMessage")
//                .isEqualTo(ResponseMessage.NOT_AVAILABLE);
//
//    }
//
//    /**
//     * 유효하지 않은 좌석에 대한 고객의 예약 요청
//     */
//    @Test
//    void 예약_요청_유효하지_않은_좌석일_시_예외_처리() {
//        // given
//        long customerId = 2;
//        ReserverInfo reserverInfo = new ReserverInfo(
//                "테스트사용자",
//                ReceiveMethod.ONLINE,
//                "테스트수령인",
//                null,
//                null,
//                null
//        );
//        TicketInfo ticketInfo = new TicketInfo(
//                1L,
//                1L,
//                BigDecimal.valueOf(10000),
//                DateUtils.createTemporalDateByIntParameters(2024, 8, 12, 23,0,0),
//                List.of(999L)
//        );
//
//        // when - then
//        assertThatThrownBy(() -> reservationFacade.requestReservation(customerId, reserverInfo, ticketInfo))
//                .isInstanceOf(CustomException.class)
//                .extracting("responseMessage")
//                .isEqualTo(ResponseMessage.NOT_AVAILABLE);
//
//    }
//
//    /**
//     * 이미 배정된 좌석에 대한 고객의 예약 요청
//     */
//    @Test
//    void 예약_요청_이미_배정된_좌석일_시_예외_처리() {
//        // given
//        long customerId = 2;
//        ReserverInfo reserverInfo = new ReserverInfo(
//                "테스트사용자",
//                ReceiveMethod.ONLINE,
//                "테스트수령인",
//                null,
//                null,
//                null
//        );
//        TicketInfo ticketInfo = new TicketInfo(
//                1L,
//                1L,
//                BigDecimal.valueOf(10000),
//                DateUtils.createTemporalDateByIntParameters(2024, 8, 12, 23,0,0),
//                List.of(9L)
//        );
//
//        // when - then
//        assertThatThrownBy(() -> reservationFacade.requestReservation(customerId, reserverInfo, ticketInfo))
//                .isInstanceOf(CustomException.class)
//                .extracting("responseMessage")
//                .isEqualTo(ResponseMessage.NOT_AVAILABLE);
//    }
//
//    /**
//     * 예약 요청 성공
//     */
//    @Test
//    void 예약_요청_모두_통과() {
//        // given
//        long customerId = 2;
//        ReserverInfo reserverInfo = new ReserverInfo(
//                "테스트사용자",
//                ReceiveMethod.ONLINE,
//                "테스트수령인",
//                null,
//                null,
//                null
//        );
//        TicketInfo ticketInfo = new TicketInfo(
//                1L,
//                1L,
//                BigDecimal.valueOf(10000),
//                DateUtils.createTemporalDateByIntParameters(2024, 8, 12, 23,0,0),
//                List.of(12L)
//        );
//
//        // when
//        ReservationResultInfoWithTickets result = reservationFacade.requestReservation(customerId, reserverInfo, ticketInfo);
//        SeatModel seatModel = seatRepository.findById(12L).orElseGet(SeatModel::noContents);
//
//        // then
//        assertAll(() -> assertEquals(customerId, result.getReservation().customerId()),
//                () -> assertEquals(ticketInfo.getConcertId(), result.getTickets().get(0).concertId()),
//                () -> assertEquals(ticketInfo.getPerformanceId(), result.getTickets().get(0).performanceId()),
//                () -> assertEquals(ticketInfo.getSeatIds().get(0), result.getTickets().get(0).seatId()),
//                () -> assertEquals(ReservationStatus.REQUESTED, result.getReservation().reservationStatus()),
//                () -> assertEquals(TicketStatus.PAY_WAITING, result.getTickets().get(0).ticketStatus()));
//        assertAll(() -> assertEquals("12", seatModel.seatNo()),
//                () -> assertEquals(SeatStatus.WAITING_FOR_RESERVATION, seatModel.seatStatus()));
//    }
//
//    /**
//     * 유효하지 않은 고객의 결제 요청
//     */
//    @Test
//    void 결제_요청_유효하지_않은_고객일_시_예외_처리() {
//        // given
//        long customerId = 999;
//        long reservationId = 10;
//        List<PaymentInfo> paymentInfos = List.of(
//                new PaymentInfo(PayMethod.POINT, BigDecimal.valueOf(10000)));
//
//        // when - then
//        assertThatThrownBy(() -> reservationFacade.requestPayment(customerId, reservationId, paymentInfos))
//                .isInstanceOf(CustomException.class)
//                .extracting("responseMessage")
//                .isEqualTo(ResponseMessage.NOT_AVAILABLE);
//    }
//
//    /**
//     * 유효하지 않은 예약의 결제 요청
//     */
//    @Test
//    void 결제_요청_존재하지_않는_예약일_시_예외_처리() {
//        // given
//        long customerId = 1;
//        long reservationId = 9999;
//        List<PaymentInfo> paymentInfos = List.of(
//                        new PaymentInfo(PayMethod.POINT, BigDecimal.valueOf(10000)));
//
//        // when - then
//        assertThatThrownBy(() -> reservationFacade.requestPayment(customerId, reservationId, paymentInfos))
//                .isInstanceOf(CustomException.class)
//                .extracting("responseMessage")
//                .isEqualTo(ResponseMessage.RESERVATION_NOT_FOUND);
//    }
//
//    /**
//     *  결제 시간이 초과된 예약의 결제 요청
//     */
//    @Test
//    void 결제_요청_결제_요청_시간이_초과된_예약일_시_예외_처리() {
//        // given
//        long customerId = 2;
//        long reservationId = 10;
//        List<PaymentInfo> paymentInfos = List.of(
//                new PaymentInfo(PayMethod.POINT, BigDecimal.valueOf(10000)));
//
//        // when - then
//        assertThatThrownBy(() -> reservationFacade.requestPayment(customerId, reservationId, paymentInfos))
//                .isInstanceOf(TimeOutCustomException.class)
//                .extracting("responseMessage")
//                .isEqualTo(ResponseMessage.PAYMENT_OUT_OF_TIME);
//
//        // when
//        ReservationModel reservationModel = reservationRepository.findByIdAndCustomerId(reservationId, customerId).orElseGet(ReservationModel::noContents);
//        List<TicketModel> ticketModels = ticketRepository.findAllByReservationIdAndTicketStatus(reservationId, TicketStatus.PAY_WAITING);
//        List<SeatModel> seatModels = seatRepository.findAllByReservationIdAndSeatStatus(reservationId, SeatStatus.WAITING_FOR_RESERVATION);
//
//        // then
//        assertEquals(ReservationStatus.CANCELLED, reservationModel.reservationStatus());
//        assertTrue(ticketModels.isEmpty());
//        assertTrue(seatModels.isEmpty());
//    }
//
//    /**
//     * 결제 취소 된 예약의 결제 요청
//     */
//    @Test
//    void 결제_요청_결제가_불가능한_상태인_예약일_시_예외_처리() {
//        // given
//        long customerId = 2;
//        long reservationId = 10;
//        List<PaymentInfo> paymentInfos = List.of(
//                new PaymentInfo(PayMethod.POINT, BigDecimal.valueOf(10000)));
//
//        // when - then
//        assertThatThrownBy(() -> reservationFacade.requestPayment(customerId, reservationId, paymentInfos))
//                .isInstanceOf(CustomException.class)
//                .extracting("responseMessage")
//                .isEqualTo(ResponseMessage.RESERVATION_NOT_AVAILABLE);
//    }
//
//    /**
//     *  잔액 부족 상태에서 예약의 결제 요청
//     */
//    @Test
//    void requestPayment_when_out_of_budget() {
//        // given
//        long customerId = 1;
//        long reservationId = 10;
//        List<PaymentInfo> paymentInfos = List.of(
//                new PaymentInfo(PayMethod.POINT, BigDecimal.valueOf(10000)));
//
//        // when - then
//        assertThatThrownBy(() -> reservationFacade.requestPayment(customerId, reservationId, paymentInfos))
//                .isInstanceOf(InsufficientResourcesCustomException.class)
//                .extracting("responseMessage")
//                .isEqualTo(ResponseMessage.OUT_OF_BUDGET);
//
//        // when
//        ReservationModel reservationModel = reservationRepository.findByIdAndCustomerId(reservationId, customerId).orElseGet(ReservationModel::noContents);
//        List<TicketModel> ticketModels = ticketRepository.findAllByReservationIdAndTicketStatus(reservationId, TicketStatus.PAY_WAITING);
//        List<SeatModel> seatModels = seatRepository.findAllByReservationIdAndSeatStatus(reservationId, SeatStatus.WAITING_FOR_RESERVATION);
//
//        // then
//        assertEquals(ReservationStatus.CANCELLED, reservationModel.reservationStatus());
//        assertTrue(ticketModels.isEmpty());
//        assertTrue(seatModels.isEmpty());
//    }
//
//    /**
//     * 결제 요청 시간이 만료된 예약의 좌석 임시 배정 취소
//     */
//    @Test
//    void 결제_요청_시간이_만료된_예약의_좌석_임시_배정_취소() {
//        // given - when
//        reservationFacade.removeWaitingReservation();
//
//        // then
//        List<ReservationModel> result = reservationRepository.findAllByReservationStatus(ReservationStatus.REQUESTED);
//        assertTrue(result.isEmpty());
//    }

}