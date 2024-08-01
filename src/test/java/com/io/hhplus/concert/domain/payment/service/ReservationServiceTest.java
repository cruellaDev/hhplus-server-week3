package com.io.hhplus.concert.domain.payment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Disabled
class ReservationServiceTest {

//    @Mock
//    private ReservationRepository reservationRepository;
//
//    @InjectMocks
//    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 예약 저장
     */
//    @Test
//    void saveReservation() {
//        // given
//        ReservationModel reservationModel = ReservationModel.create(
//                1L,
//                1L,
//                "김항해",
//                ReservationStatus.REQUESTED,
//                new Date(),
//                ReceiveMethod.ONLINE,
//                "김항해",
//                null,
//                null,
//                null
//        );
//        given(reservationRepository.save(any(ReservationModel.class))).willReturn(reservationModel);
//
//        // when
//        ReservationModel result = reservationService.saveReservation(reservationModel);
//
//        // then
//        assertAll(() -> assertEquals(reservationModel.reservationId(), result.reservationId()),
//                () -> assertEquals(reservationModel.customerId(), result.customerId()),
//                () -> assertEquals(reservationModel.reserverName(), result.reserverName()),
//                () -> assertEquals(reservationModel.receiveMethod(), result.receiveMethod()),
//                () -> assertEquals(reservationModel.receiverName(), result.receiverName()));
//    }
//
//    /**
//     * 예약 조회
//     */
//    @Test
//    void getReservation() {
//        // given
//        ReservationModel reservationModel = ReservationModel.create(
//                1L,
//                1L,
//                "김항해",
//                ReservationStatus.REQUESTED,
//                new Date(),
//                ReceiveMethod.ONLINE,
//                "김항해",
//                null,
//                null,
//                null
//        );
//        given(reservationRepository.findByIdAndCustomerId(anyLong(), anyLong())).willReturn(Optional.of(reservationModel));
//
//        // when
//        ReservationModel result = reservationService.getReservation(1L, 1L);
//
//        // then
//        assertAll(() -> assertEquals(reservationModel.reservationId(), result.reservationId()),
//                () -> assertEquals(reservationModel.customerId(), result.customerId()),
//                () -> assertEquals(reservationModel.reserverName(), result.reserverName()),
//                () -> assertEquals(reservationModel.receiveMethod(), result.receiveMethod()),
//                () -> assertEquals(reservationModel.receiverName(), result.receiverName()));
//    }
//
//    /**
//     * 티켓 합계 조회
//     */
//    @Test
//    void getSumOfTicketPrice() {
//        // given
//        BigDecimal sumOfTicketPrice = BigDecimal.valueOf(50000);
//        given(ticketRepository.sumTicketPriceByReservationIdAndTicketStatus(anyLong(), any())).willReturn(sumOfTicketPrice);
//
//        // when
//        BigDecimal result = reservationService.getSumOfTicketPrice(1L, TicketStatus.PAY_WAITING);
//
//        // then
//        assertThat(result).isEqualTo(sumOfTicketPrice);
//    }
//
//    /**
//     * 예약 상태 변경
//     */
//    @Test
//    void updateReservationStatus() {
//        // given
//        ReservationModel asisReservationModel = ReservationModel.create(
//                1L,
//                1L,
//                "김항해",
//                ReservationStatus.REQUESTED,
//                DateUtils.createTemporalDateByIntParameters(2024,7,12,11,58,33),
//                ReceiveMethod.ONLINE,
//                "김항해",
//                null,
//                null,
//                null
//        );
//        ReservationModel tobeReservationModel = ReservationModel.changeStatus(asisReservationModel, ReservationStatus.COMPLETED);
//        given(reservationRepository.findByIdAndCustomerId(anyLong(), anyLong())).willReturn(Optional.of(asisReservationModel));
//        given(reservationRepository.save(any(ReservationModel.class))).willReturn(tobeReservationModel);
//
//        // when
//        ReservationModel result = reservationService.updateReservationStatus(1L, 1L, ReservationStatus.COMPLETED);
//
//        // then
//        assertThat(result.reservationStatus()).isEqualTo(tobeReservationModel.reservationStatus());
//
//    }
//
//    /**
//     * 티켓 상태 변경
//     */
//    @Test
//    void updateTicketsStatus() {
//        // given
//        Date sysdate = DateUtils.getSysDate();
//        List<TicketModel> asisTicketModel = List.of(
//                TicketModel.create(
//                        1L,
//                        1L,
//                        1L,
//                        1L,
//                        1L,
//                        "항해콘서트",
//                        "김항해",
//                        true,
//                        true,
//                        true,
//                        BigDecimal.valueOf(40000),
//                        DateUtils.createTemporalDateByIntParameters(2024, 11, 11, 11, 11, 11),
//                        "50",
//                        TicketStatus.PAY_WAITING,
//                        null,
//                        null,
//                        null,
//                        null,
//                        null
//                )
//        );
//        List<TicketModel> tobeTicketModel = List.of(
//                TicketModel.create(
//                        1L,
//                        1L,
//                        1L,
//                        1L,
//                        1L,
//                        "항해콘서트",
//                        "김항해",
//                        true,
//                        true,
//                        true,
//                        BigDecimal.valueOf(40000),
//                        DateUtils.createTemporalDateByIntParameters(2024, 11, 11, 11, 11, 11),
//                        "50",
//                        TicketStatus.PAYED,
//                        sysdate,
//                        sysdate,
//                        null,
//                        null,
//                        null
//                )
//        );
//        given(ticketRepository.findAllByReservationIdAndTicketStatus(anyLong(), any())).willReturn(asisTicketModel);
//        given(ticketRepository.saveAll(any())).willReturn(tobeTicketModel);
//
//        // when
//        List<TicketModel> result = reservationService.updateTicketsStatus(1L, TicketStatus.PAY_WAITING, TicketStatus.PAYED);
//
//        // then
//        assertThat(result.get(0).ticketStatus()).isEqualTo(tobeTicketModel.get(0).ticketStatus());
//    }
//
//    /**
//     * 예약 상태값으로 모든 예약 조회
//     */
//    @Test
//    void getAllReservationByReservationStatus() {
//        // given
//        List<ReservationModel> reservationModels = List.of(
//                ReservationModel.create(
//                        1L,
//                        1L,
//                        "김항해",
//                        ReservationStatus.REQUESTED,
//                        DateUtils.createTemporalDateByIntParameters(2024,7,12,11,58,33),
//                        ReceiveMethod.ONLINE,
//                        "김항해",
//                        null,
//                        null,
//                        null
//                )
//        );
//        given(reservationRepository.findAllByReservationStatus(any())).willReturn(reservationModels);
//
//        // when
//        List<ReservationModel> result = reservationService.getAllReservationByReservationStatus(ReservationStatus.REQUESTED);
//
//        // then
//        assertThat(result).hasSize(1);
//        assertThat(result.get(0).reservationStatus()).isEqualTo(ReservationStatus.REQUESTED);
//    }

}