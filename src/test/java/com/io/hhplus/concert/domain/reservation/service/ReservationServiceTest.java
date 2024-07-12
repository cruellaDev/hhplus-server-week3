package com.io.hhplus.concert.domain.reservation.service;

import com.io.hhplus.concert.common.enums.ReceiveMethod;
import com.io.hhplus.concert.common.enums.ReservationStatus;
import com.io.hhplus.concert.common.enums.TicketStatus;
import com.io.hhplus.concert.common.utils.DateUtils;
import com.io.hhplus.concert.domain.reservation.model.Reservation;
import com.io.hhplus.concert.domain.reservation.model.Ticket;
import com.io.hhplus.concert.domain.reservation.repository.ReservationRepository;
import com.io.hhplus.concert.domain.reservation.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 예약 저장
     */
    @Test
    void saveReservation() {
        // given
        Reservation reservation = Reservation.create(
                1L,
                1L,
                "김항해",
                ReservationStatus.REQUESTED,
                new Date(),
                ReceiveMethod.ONLINE,
                "김항해",
                null,
                null,
                null
        );
        given(reservationRepository.save(any(Reservation.class))).willReturn(reservation);

        // when
        Reservation result = reservationService.saveReservation(reservation);

        // then
        assertAll(() -> assertEquals(reservation.reservationId(), result.reservationId()),
                () -> assertEquals(reservation.customerId(), result.customerId()),
                () -> assertEquals(reservation.reserverName(), result.reserverName()),
                () -> assertEquals(reservation.receiveMethod(), result.receiveMethod()),
                () -> assertEquals(reservation.receiverName(), result.receiverName()));
    }

    /**
     * 예약 조회
     */
    @Test
    void getReservation() {
        // given
        Reservation reservation = Reservation.create(
                1L,
                1L,
                "김항해",
                ReservationStatus.REQUESTED,
                new Date(),
                ReceiveMethod.ONLINE,
                "김항해",
                null,
                null,
                null
        );
        given(reservationRepository.findByIdAndCustomerId(anyLong(), anyLong())).willReturn(Optional.of(reservation));

        // when
        Reservation result = reservationService.getReservation(1L, 1L);

        // then
        assertAll(() -> assertEquals(reservation.reservationId(), result.reservationId()),
                () -> assertEquals(reservation.customerId(), result.customerId()),
                () -> assertEquals(reservation.reserverName(), result.reserverName()),
                () -> assertEquals(reservation.receiveMethod(), result.receiveMethod()),
                () -> assertEquals(reservation.receiverName(), result.receiverName()));
    }

    /**
     * 예약 유효성 검증 - 모두 통과 시
     */
    @Test
    void meetsIfReservationAvailable_when_pass_all() {
        // given
        Reservation reservation = Reservation.create(
                1L,
                1L,
                "김항해",
                ReservationStatus.REQUESTED,
                new Date(),
                ReceiveMethod.ONLINE,
                "김항해",
                null,
                null,
                null
        );

        // when
        boolean isValid = reservationService.meetsIfReservationAvailable(reservation);

        // then
        assertThat(isValid).isTrue();
    }

    /**
     * 예약 유효성 검증 - 예약_ID 이상할 시
     */
    @Test
    void meetsIfReservationAvailable_when_reservationId_is_wrong() {
        // given
        Reservation reservation = Reservation.create(
                -1L,
                1L,
                "김항해",
                ReservationStatus.REQUESTED,
                new Date(),
                ReceiveMethod.ONLINE,
                "김항해",
                null,
                null,
                null
        );

        // when
        boolean isValid = reservationService.meetsIfReservationAvailable(reservation);

        // then
        assertThat(isValid).isFalse();
    }

    /**
     * 예약 유효성 검증 - 예매자 명 이상할 시
     */
    @Test
    void meetsIfReservationAvailable_when_reserverName_is_wrong() {
        // given
        Reservation reservation = Reservation.create(
                1L,
                1L,
                "",
                ReservationStatus.REQUESTED,
                new Date(),
                ReceiveMethod.ONLINE,
                "김항해",
                null,
                null,
                null
        );

        // when
        boolean isValid = reservationService.meetsIfReservationAvailable(reservation);

        // then
        assertThat(isValid).isFalse();
    }

    /**
     * 예약 유효성 검증 - 수령방법 이상할 시
     */
    @Test
    void meetsIfReservationAvailable_when_receiveMethod_is_wrong() {
        // given
        Reservation reservation = Reservation.create(
                1L,
                1L,
                "김항해",
                ReservationStatus.REQUESTED,
                new Date(),
                null,
                "김항해",
                null,
                null,
                null
        );

        // when
        boolean isValid = reservationService.meetsIfReservationAvailable(reservation);

        // then
        assertThat(isValid).isFalse();
    }

    /**
     * 예약 유효성 검증 - 수령인 명 이상할 시
     */
    @Test
    void meetsIfReservationAvailable_when_receiveName_is_wrong() {
        // given
        Reservation reservation = Reservation.create(
                1L,
                1L,
                "김항해",
                ReservationStatus.REQUESTED,
                new Date(),
                ReceiveMethod.ONLINE,
                "",
                null,
                null,
                null
        );

        // when
        boolean isValid = reservationService.meetsIfReservationAvailable(reservation);

        // then
        assertThat(isValid).isFalse();
    }

    /**
     * 예약 유효성 검증 - 예약상태 이상할 시
     */
    @Test
    void meetsIfReservationAvailable_when_reservationStatus_is_wrong() {
        // given
        Reservation reservation = Reservation.create(
                1L,
                1L,
                "김항해",
                null,
                new Date(),
                ReceiveMethod.ONLINE,
                "김항해",
                null,
                null,
                null
        );

        // when
        boolean isValid = reservationService.meetsIfReservationAvailable(reservation);

        // then
        assertThat(isValid).isFalse();
    }

    /**
     * 예약 정보 결제 유효 기간 내 존재 여부 확인 -기간 내 일시
     */
    @Test
    void meetsIfAbleToPayInTimeLimits() {
        // given
        Date targetDate = DateUtils.createTemporalDateByIntParameters(2024, 7, 12, 11, 50, 22);

        // when
        boolean isValid = reservationService.meetsIfAbleToPayInTimeLimits(300L, targetDate);

        // then
        assertThat(isValid).isTrue();
    }

    /**
     * 예약 정보 결제 유효 기간 내 존재 여부 확인 -이미 지났을 시
     */
    @Test
    void meetsIfAbleToPayInTimeLimits_when_passed() {
        // given
        Date targetDate = DateUtils.createTemporalDateByIntParameters(2024, 7, 12, 11, 47, 22);

        // when
        boolean isValid = reservationService.meetsIfAbleToPayInTimeLimits(300L, targetDate);

        // then
        assertThat(isValid).isFalse();
    }

    /**
     * 티켓 합계 조회
     */
    @Test
    void getSumOfTicketPrice() {
        // given
        BigDecimal sumOfTicketPrice = BigDecimal.valueOf(50000);
        given(ticketRepository.sumTicketPriceByReservationIdAndTicketStatus(anyLong(), any())).willReturn(sumOfTicketPrice);

        // when
        BigDecimal result = reservationService.getSumOfTicketPrice(1L, TicketStatus.PAY_WAITING);

        // then
        assertThat(result).isEqualTo(sumOfTicketPrice);
    }

    /**
     * 예약 상태 변경
     */
    @Test
    void updateReservationStatus() {
        // given
        Reservation asisReservation = Reservation.create(
                1L,
                1L,
                "김항해",
                ReservationStatus.REQUESTED,
                DateUtils.createTemporalDateByIntParameters(2024,7,12,11,58,33),
                ReceiveMethod.ONLINE,
                "김항해",
                null,
                null,
                null
        );
        Reservation tobeReservation = Reservation.changeStatus(asisReservation, ReservationStatus.COMPLETED);
        given(reservationRepository.findByIdAndCustomerId(anyLong(), anyLong())).willReturn(Optional.of(asisReservation));
        given(reservationRepository.save(any(Reservation.class))).willReturn(tobeReservation);

        // when
        Reservation result = reservationService.updateReservationStatus(1L, 1L, ReservationStatus.COMPLETED);

        // then
        assertThat(result.reservationStatus()).isEqualTo(tobeReservation.reservationStatus());

    }

    /**
     * 티켓 상태 변경
     */
    @Test
    void updateTicketsStatus() {
        // given
        Date sysdate = DateUtils.getSysDate();
        List<Ticket> asisTicket = List.of(
                Ticket.create(
                        1L,
                        1L,
                        1L,
                        1L,
                        1L,
                        "항해콘서트",
                        "김항해",
                        true,
                        true,
                        true,
                        BigDecimal.valueOf(40000),
                        DateUtils.createTemporalDateByIntParameters(2024, 11, 11, 11, 11, 11),
                        "50",
                        TicketStatus.PAY_WAITING,
                        null,
                        null,
                        null,
                        null,
                        null
                )
        );
        List<Ticket> tobeTicket = List.of(
                Ticket.create(
                        1L,
                        1L,
                        1L,
                        1L,
                        1L,
                        "항해콘서트",
                        "김항해",
                        true,
                        true,
                        true,
                        BigDecimal.valueOf(40000),
                        DateUtils.createTemporalDateByIntParameters(2024, 11, 11, 11, 11, 11),
                        "50",
                        TicketStatus.PAYED,
                        sysdate,
                        sysdate,
                        null,
                        null,
                        null
                )
        );
        given(ticketRepository.findAllByReservationIdAndTicketStatus(anyLong(), any())).willReturn(asisTicket);
        given(ticketRepository.saveAll(any())).willReturn(tobeTicket);

        // when
        List<Ticket> result = reservationService.updateTicketsStatus(1L, TicketStatus.PAY_WAITING, TicketStatus.PAYED);

        // then
        assertThat(result.get(0).ticketStatus()).isEqualTo(tobeTicket.get(0).ticketStatus());
    }

    /**
     * 예약 상태값으로 모든 예약 조회
     */
    @Test
    void getAllReservationByReservationStatus() {
        // given
        List<Reservation> reservations = List.of(
                Reservation.create(
                        1L,
                        1L,
                        "김항해",
                        ReservationStatus.REQUESTED,
                        DateUtils.createTemporalDateByIntParameters(2024,7,12,11,58,33),
                        ReceiveMethod.ONLINE,
                        "김항해",
                        null,
                        null,
                        null
                )
        );
        given(reservationRepository.findAllByReservationStatus(any())).willReturn(reservations);

        // when
        List<Reservation> result = reservationService.getAllReservationByReservationStatus(ReservationStatus.REQUESTED);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).reservationStatus()).isEqualTo(ReservationStatus.REQUESTED);
    }

}