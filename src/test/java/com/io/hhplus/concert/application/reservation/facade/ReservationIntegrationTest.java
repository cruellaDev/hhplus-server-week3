package com.io.hhplus.concert.application.reservation.facade;

import com.io.hhplus.concert.application.reservation.dto.PaymentRequest;
import com.io.hhplus.concert.application.reservation.dto.ReservationRequest;
import com.io.hhplus.concert.application.reservation.dto.ReservationResponse;
import com.io.hhplus.concert.common.enums.*;
import com.io.hhplus.concert.common.exceptions.IllegalStateCustomException;
import com.io.hhplus.concert.common.exceptions.InsufficientResourcesCustomException;
import com.io.hhplus.concert.common.exceptions.ResourceNotFoundCustomException;
import com.io.hhplus.concert.common.exceptions.TimeOutCustomException;
import com.io.hhplus.concert.common.utils.DateUtils;
import com.io.hhplus.concert.domain.concert.model.Seat;
import com.io.hhplus.concert.domain.concert.repository.SeatRepository;
import com.io.hhplus.concert.domain.reservation.model.Reservation;
import com.io.hhplus.concert.domain.reservation.model.Ticket;
import com.io.hhplus.concert.domain.reservation.repository.ReservationRepository;
import com.io.hhplus.concert.domain.reservation.repository.TicketRepository;
import com.io.hhplus.concert.domain.waiting.model.WaitingEnterHistory;
import com.io.hhplus.concert.domain.waiting.model.WaitingQueue;
import com.io.hhplus.concert.domain.waiting.repository.WaitingRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReservationIntegrationTest {

    @Autowired
    private ReservationFacade reservationFacade;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private WaitingRepository waitingRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @BeforeEach
    void setUp() {
        for (long i = 2; i < 10; i++) {
            WaitingQueue waitingQueue = waitingRepository.saveWaitingQueue(WaitingQueue.create(null, i, "userToken" + (new Date()).toString(), WaitingStatus.ACTIVE, null, null));
            waitingRepository.saveWaitingEnterHistory(WaitingEnterHistory.create(null, waitingQueue.waitingId(), null));
        }

        for (long i = 4; i < 50; i+=5) {
            seatRepository.save(Seat.create(i, 1L, 1L, String.valueOf(i), SeatStatus.WAITING_FOR_RESERVATION));
        }
    }

    @AfterEach
    void tearDown() {
        waitingRepository.deleteAllWaitingQueue();
        waitingRepository.deleteAllWaitingEnterHistory();

        for (long i = 4; i < 50; i+=5) {
            seatRepository.save(Seat.create(i, 1L, 1L, String.valueOf(i), SeatStatus.AVAILABLE));
        }
    }

    /**
     * 유효하지 않은 고객의 예약 요청
     */
    @Test
    void requestReservation_unavailable_customer() {
        // given
        ReservationRequest request = new ReservationRequest(
                new ReservationRequest.Reserver(9999L, "테스트사용자"),
                new ReservationRequest.Receiver(ReceiveMethod.ONLINE, "테스트수령인", null, null, null),
                new ReservationRequest.Concert(1L, "항해콘서트"),
                new ReservationRequest.Performance(1L, DateUtils.createTemporalDateByIntParameters(2024, 8, 12, 23,0,0), BigDecimal.valueOf(10000)),
                List.of(new ReservationRequest.Seat(3L, "03"))
        );

        // when - then
        assertThatThrownBy(() -> reservationFacade.requestReservation(request))
                .isInstanceOf(ResourceNotFoundCustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_AVAILABLE);

    }

    /**
     * 대기열에 없는 고객의 예약 요청
     */
    @Test
    void requestReservation_not_waiting_customer() {
        // given
        ReservationRequest request = new ReservationRequest(
                new ReservationRequest.Reserver(1L, "테스트사용자"),
                new ReservationRequest.Receiver(ReceiveMethod.ONLINE, "테스트수령인", null, null, null),
                new ReservationRequest.Concert(1L, "항해콘서트"),
                new ReservationRequest.Performance(1L, DateUtils.createTemporalDateByIntParameters(2024, 8, 12, 23,0,0), BigDecimal.valueOf(10000)),
                List.of(new ReservationRequest.Seat(3L, "03"))
        );

        // when - then
        assertThatThrownBy(() -> reservationFacade.requestReservation(request))
                .isInstanceOf(ResourceNotFoundCustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_FOUND);

    }

    /**
     * 유효하지 않은 콘서트에 대한 고객의 예약 요청
     */
    @Test
    void requestReservation_unavailable_concert() {
        // given
        ReservationRequest request = new ReservationRequest(
                new ReservationRequest.Reserver(2L, "테스트사용자"),
                new ReservationRequest.Receiver(ReceiveMethod.ONLINE, "테스트수령인", null, null, null),
                new ReservationRequest.Concert(999L, "항해콘서트"),
                new ReservationRequest.Performance(1L, DateUtils.createTemporalDateByIntParameters(2024, 8, 12, 23,0,0), BigDecimal.valueOf(10000)),
                List.of(new ReservationRequest.Seat(3L, "03"))
        );

        // when - then
        assertThatThrownBy(() -> reservationFacade.requestReservation(request))
                .isInstanceOf(IllegalStateCustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_AVAILABLE);

    }

    /**
     * 유효하지 않은 공연에 대한 고객의 예약 요청
     */
    @Test
    void requestReservation_unavailable_performance() {
        // given
        ReservationRequest request = new ReservationRequest(
                new ReservationRequest.Reserver(2L, "테스트사용자"),
                new ReservationRequest.Receiver(ReceiveMethod.ONLINE, "테스트수령인", null, null, null),
                new ReservationRequest.Concert(1L, "항해콘서트"),
                new ReservationRequest.Performance(999L, DateUtils.createTemporalDateByIntParameters(2024, 8, 12, 23,0,0), BigDecimal.valueOf(10000)),
                List.of(new ReservationRequest.Seat(3L, "03"))
        );

        // when - then
        assertThatThrownBy(() -> reservationFacade.requestReservation(request))
                .isInstanceOf(IllegalStateCustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_AVAILABLE);

    }

    /**
     * 유효하지 않은 좌석에 대한 고객의 예약 요청
     */
    @Test
    void requestReservation_unavailable_seats() {
        // given
        ReservationRequest request = new ReservationRequest(
                new ReservationRequest.Reserver(2L, "테스트사용자"),
                new ReservationRequest.Receiver(ReceiveMethod.ONLINE, "테스트수령인", null, null, null),
                new ReservationRequest.Concert(1L, "항해콘서트"),
                new ReservationRequest.Performance(1L, DateUtils.createTemporalDateByIntParameters(2024, 8, 12, 23,0,0), BigDecimal.valueOf(10000)),
                List.of(new ReservationRequest.Seat(999L, "999"))
        );

        // when - then
        assertThatThrownBy(() -> reservationFacade.requestReservation(request))
                .isInstanceOf(IllegalStateCustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_AVAILABLE);

    }

    /**
     * 이미 배정된 좌석에 대한 고객의 예약 요청
     */
    @Test
    void requestReservation_occupied_seats() {
        // given
        ReservationRequest request = new ReservationRequest(
                new ReservationRequest.Reserver(2L, "테스트사용자"),
                new ReservationRequest.Receiver(ReceiveMethod.ONLINE, "테스트수령인", null, null, null),
                new ReservationRequest.Concert(1L, "항해콘서트"),
                new ReservationRequest.Performance(1L, DateUtils.createTemporalDateByIntParameters(2024, 8, 12, 23,0,0), BigDecimal.valueOf(10000)),
                List.of(new ReservationRequest.Seat(9L, "09"))
        );

        // when - then
        assertThatThrownBy(() -> reservationFacade.requestReservation(request))
                .isInstanceOf(IllegalStateCustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_AVAILABLE);
    }

    /**
     * 예약 요청 성공
     */
    @Test
    void requestReservation_success() {
        // given
        ReservationRequest request = new ReservationRequest(
                new ReservationRequest.Reserver(2L, "테스트사용자"),
                new ReservationRequest.Receiver(ReceiveMethod.ONLINE, "테스트수령인", null, null, null),
                new ReservationRequest.Concert(1L, "항해콘서트"),
                new ReservationRequest.Performance(1L, DateUtils.createTemporalDateByIntParameters(2024, 8, 12, 23,0,0), BigDecimal.valueOf(10000)),
                List.of(new ReservationRequest.Seat(12L, "12"))
        );

        // when
        ReservationResponse result = reservationFacade.requestReservation(request);
        Seat seat = seatRepository.findById(12L).orElseGet(Seat::noContents);

        // then
        assertAll(() -> assertEquals(request.getReserver().getCustomerId(), result.getReservation().customerId()),
                () -> assertEquals(request.getConcert().getConcertId(), result.getTickets().get(0).concertId()),
                () -> assertEquals(request.getPerformance().getPerformanceId(), result.getTickets().get(0).performanceId()),
                () -> assertEquals(request.getSeats().get(0).getSeatId(), result.getTickets().get(0).seatId()),
                () -> assertEquals(ReservationStatus.REQUESTED, result.getReservation().reservationStatus()),
                () -> assertEquals(TicketStatus.PAY_WAITING, result.getTickets().get(0).ticketStatus()));
        assertAll(() -> assertEquals("12", seat.seatNo()),
                () -> assertEquals(SeatStatus.WAITING_FOR_RESERVATION, seat.seatStatus()));
    }

    /**
     * 유효하지 않은 고객의 결제 요청
     */
    @Test
    void requestPayment_when_unavailable_customer() {
        // given
        PaymentRequest request = new PaymentRequest(
                3L, 10L,
                List.of(
                        new PaymentRequest.PayInfo(PayMethod.POINT, BigDecimal.valueOf(10000))
                )
        );

        // when - then
        assertThatThrownBy(() -> reservationFacade.requestPayment(request))
                .isInstanceOf(ResourceNotFoundCustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_AVAILABLE);
    }

    /**
     * 유효하지 않은 예약의 결제 요청
     */
    @Test
    void requestPayment_when_unavailable_reservation() {
        // given
        PaymentRequest request = new PaymentRequest(
                1L, 11L,
                List.of(
                        new PaymentRequest.PayInfo(PayMethod.POINT, BigDecimal.valueOf(10000))
                )
        );

        // when - then
        assertThatThrownBy(() -> reservationFacade.requestPayment(request))
                .isInstanceOf(ResourceNotFoundCustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.NOT_AVAILABLE);
    }

    /**
     *  결제 시간이 초과된 예약의 결제 요청
     */
    @Test
    void requestPayment_when_timeout_reservation() {
        // given
        PaymentRequest request = new PaymentRequest(
                2L, 10L,
                List.of(
                        new PaymentRequest.PayInfo(PayMethod.POINT, BigDecimal.valueOf(10000))
                )
        );

        // when - then
        assertThatThrownBy(() -> reservationFacade.requestPayment(request))
                .isInstanceOf(TimeOutCustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.OUT_OF_TIME);

        // when
        Reservation reservation = reservationRepository.findByIdAndCustomerId(10L, 1L).orElseGet(Reservation::noContents);
        List<Ticket> tickets = ticketRepository.findAllByReservationIdAndTicketStatus(10L, TicketStatus.PAY_WAITING);
        List<Seat> seats = seatRepository.findAllByReservationIdAndSeatStatus(10L, SeatStatus.WAITING_FOR_RESERVATION);

        // then
        assertEquals(ReservationStatus.CANCELLED, reservation.reservationStatus());
        assertTrue(tickets.isEmpty());
        assertTrue(seats.isEmpty());
    }

    /**
     *  잔액 부족 상태에서 예약의 결제 요청
     */
    @Test
    void requestPayment_when_out_of_budget() {
        // given
        PaymentRequest request = new PaymentRequest(
                2L, 10L,
                List.of(
                        new PaymentRequest.PayInfo(PayMethod.POINT, BigDecimal.valueOf(10000))
                )
        );

        // when - then
        assertThatThrownBy(() -> reservationFacade.requestPayment(request))
                .isInstanceOf(InsufficientResourcesCustomException.class)
                .extracting("responseMessage")
                .isEqualTo(ResponseMessage.OUT_OF_BUDGET);

        // when
        Reservation reservation = reservationRepository.findByIdAndCustomerId(10L, 1L).orElseGet(Reservation::noContents);
        List<Ticket> tickets = ticketRepository.findAllByReservationIdAndTicketStatus(10L, TicketStatus.PAY_WAITING);
        List<Seat> seats = seatRepository.findAllByReservationIdAndSeatStatus(10L, SeatStatus.WAITING_FOR_RESERVATION);

        // then
        assertEquals(ReservationStatus.CANCELLED, reservation.reservationStatus());
        assertTrue(tickets.isEmpty());
        assertTrue(seats.isEmpty());
    }

}