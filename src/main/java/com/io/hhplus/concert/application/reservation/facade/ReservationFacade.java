package com.io.hhplus.concert.application.reservation.facade;

import com.io.hhplus.concert.application.reservation.dto.PaymentRequest;
import com.io.hhplus.concert.application.reservation.dto.PaymentResponse;
import com.io.hhplus.concert.application.reservation.dto.ReservationRequest;
import com.io.hhplus.concert.application.reservation.dto.ReservationResponse;
import com.io.hhplus.concert.common.enums.*;
import com.io.hhplus.concert.common.exceptions.*;
import com.io.hhplus.concert.domain.concert.model.Concert;
import com.io.hhplus.concert.domain.concert.model.Performance;
import com.io.hhplus.concert.domain.concert.model.Seat;
import com.io.hhplus.concert.domain.concert.service.ConcertService;
import com.io.hhplus.concert.domain.concert.service.PerformanceService;
import com.io.hhplus.concert.domain.concert.service.SeatService;
import com.io.hhplus.concert.domain.customer.model.Customer;
import com.io.hhplus.concert.domain.customer.service.CustomerService;
import com.io.hhplus.concert.domain.reservation.model.Payment;
import com.io.hhplus.concert.domain.reservation.model.Reservation;
import com.io.hhplus.concert.domain.reservation.model.Ticket;
import com.io.hhplus.concert.domain.reservation.service.PaymentService;
import com.io.hhplus.concert.domain.reservation.service.ReservationService;
import com.io.hhplus.concert.domain.waiting.model.WaitingQueue;
import com.io.hhplus.concert.domain.waiting.service.WaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ReservationFacade {

    private final CustomerService customerService;
    private final WaitingService waitingService;
    private final ConcertService concertService;
    private final PerformanceService performanceService;
    private final SeatService seatService;
    private final ReservationService reservationService;
    private final PaymentService paymentService;

    /**
     * 좌석 예약 요청
     * @param reservationRequest 예약 요청 정보
     * @return 예약 요청 결과
     */
    @Transactional
    public ReservationResponse requestReservation(ReservationRequest reservationRequest) {
        // request body 확인
        if (reservationRequest == null) {
            throw new IllegalArgumentCustomException("예약 요청 정보가 없습니다.", ResponseMessage.NOT_FOUND);
        }
        // 예약자 정보
        ReservationRequest.Reserver requestedReserver = reservationRequest.getReserver();
        if (requestedReserver == null) {
            throw new IllegalArgumentCustomException("예약 요청자 정보가 없습니다.", ResponseMessage.NOT_FOUND);
        }
        if (!Reservation.isAvailableReserverName(requestedReserver.getReserverName())) {
            throw new IllegalArgumentCustomException("예매자 명이 잘못되었습니다.", ResponseMessage.INVALID);
        }
        if (!Customer.isAvailableCustomerId(requestedReserver.getCustomerId())) {
            throw new IllegalArgumentCustomException("고객 ID 값이 잘못 되었습니다.", ResponseMessage.INVALID);
        }
        // 수령인 정보
        ReservationRequest.Receiver requestedReceiver = reservationRequest.getReceiver();
        if (requestedReceiver == null) {
            throw new IllegalArgumentCustomException("수령인 정보가 없습니다.", ResponseMessage.NOT_FOUND);
        }
        if (!Reservation.isAvailableReceiverName(requestedReceiver.getReceiverName())) {
            throw new IllegalArgumentCustomException("수령인 명이 잘못되었습니다.", ResponseMessage.INVALID);
        }
        if (!Reservation.isAvailableReceiveMethod(requestedReceiver.getReceiveMethod())) {
            throw new IllegalArgumentCustomException("수령방법이 없습니다.", ResponseMessage.NOT_FOUND);
        }
        // 콘서트 정보
        ReservationRequest.Concert requestedConcert = reservationRequest.getConcert();
        if (requestedConcert == null) {
            throw new IllegalArgumentCustomException("예약할 콘서트 정보가 존재하지 않습니다.", ResponseMessage.NOT_FOUND);
        }
        if (!Concert.isAvailableConcertId(requestedConcert.getConcertId())) {
            throw new IllegalArgumentCustomException("콘서트 ID 값이 잘못 되었습니다.", ResponseMessage.INVALID);
        }
        // 공연 정보
        ReservationRequest.Performance requestedPerformance = reservationRequest.getPerformance();
        if (requestedPerformance == null) {
            throw new IllegalArgumentCustomException("예약할 공연 정보가 존재하지 않습니다", ResponseMessage.NOT_FOUND);
        }
        if (!Performance.isAvailablePerformanceId(requestedPerformance.getPerformanceId())) {
            throw new IllegalArgumentCustomException("공연 ID 값이 잘못 되었습니다.", ResponseMessage.INVALID);
        }
        List<ReservationRequest.Seat> requestedSeats = reservationRequest.getSeats();
        if (requestedSeats == null || requestedSeats.isEmpty()) {
            throw new IllegalArgumentCustomException("예약할 좌석 정보가 존재하지 않습니다.", ResponseMessage.NOT_FOUND);
        }
        if (requestedSeats.stream().anyMatch(requestedSeat -> !Seat.isAvailableSeatId(requestedSeat.getSeatId()))) {
            throw new IllegalArgumentCustomException("좌석 ID 값이 잘못되었습니다.", ResponseMessage.INVALID);
        }

        // 고객 확인
        Customer customer;
        boolean isAvailable;

        // 고객 조회
        customer = customerService.getAvailableCustomer(requestedReserver.getCustomerId());
        // 고객 검증
        isAvailable = customerService.meetsIfCustomerValid(customer);
        if (!isAvailable) {
            throw new ResourceNotFoundCustomException("존재하지 않는 고객입니다.", ResponseMessage.NOT_AVAILABLE);
        }

        // 대기열 토큰 확인
        WaitingQueue waitingQueue;
        boolean isExists;
        boolean isValid;
        // 대기열 조회
        waitingQueue = waitingService.getActiveWaitingTokenByCustomerId(requestedReserver.getCustomerId());

        // 대기열 존재 여부 확인
        isExists = waitingService.meetsIfActiveWaitingQueueExists(waitingQueue);

        // 토큰 활성 시간 확인
        isValid = waitingService.meetsIfActiveWaitingQueueInTimeLimits(300L, waitingQueue.createdAt());
        // 유효 대기열 없을 시
        if (!isExists && !isValid) {
            throw new ResourceNotFoundCustomException("대기열 진입 정보가 존재하지 않습니다.", ResponseMessage.NOT_FOUND);
        }

        // 콘서트 확인
        Concert concert;
        // 콘서트 조회
        concert = concertService.getAvailableConcert(requestedConcert.getConcertId());
        // 콘서트 검증
        isAvailable = concertService.meetsIfConcertToBeReserved(concert);
        if (!isAvailable) {
            throw new IllegalStateCustomException("해당 콘서트는 예약이 불가능합니다.", ResponseMessage.NOT_AVAILABLE);
        }

        // 공연 확인
        Performance performance;
        // 공연 조회
        performance = performanceService.getAvailablePerformance(requestedConcert.getConcertId(), requestedPerformance.getPerformanceId());
        // 공연 검증
        isAvailable = performanceService.meetsIfPerformanceToBeReserved(performance);
        if (!isAvailable) {
            throw new IllegalStateCustomException("해당 공연은 예약이 불가능합니다.", ResponseMessage.NOT_AVAILABLE);
        }

        // 좌석 확인
        List<Seat> seats = new ArrayList<>();
        // 좌석 조회
        for (ReservationRequest.Seat requestedSeat : requestedSeats) {
            Seat seat = seatService.getAvailableSeat(requestedPerformance.getPerformanceId(), requestedSeat.getSeatId());
            if (Seat.isTaken(seat.seatStatus()) || Seat.isOccupied(seat.seatStatus())) {
                throw new IllegalStateCustomException("이미 선점된 좌석입니다.", ResponseMessage.NOT_AVAILABLE);
            }
            isAvailable = seatService.meetsIfSeatToBeReserved(seat);
            if (!isAvailable) {
                throw new IllegalStateCustomException("해당 좌석은 예약이 불가능합니다. " + requestedSeat, ResponseMessage.NOT_AVAILABLE);
            }
            seats.add(seat);
        }

        // 좌석 임시 배정
        boolean isAssigned;
        for (Seat seat : seats) {
            isAssigned = seatService.assignSeatTemporarily(seat.seatId());
            if (!isAssigned) {
                throw new IllegalStateCustomException("좌석 예약에 실패했습니다.", ResponseMessage.FAIL);
            }
        }

        // 토큰 대기 만료 처리
        boolean isExpired;
        isExpired = waitingService.expireWaitingQueueToken(waitingQueue);
        if (!isExpired) {
            throw new IllegalStateCustomException("오류가 발생하였습니다 [대기열 토큰]", ResponseMessage.FAIL);
        }

        // 예약 및 티켓 생성
        Reservation reservation;
        List<Ticket> tickets = new ArrayList<>();
        // 예약 저장
        reservation = reservationService.saveReservation(ReservationRequest.makeReservationOf(customer, requestedReserver, requestedReceiver));
        // 티켓 저장
        for (Seat seat : seats) {
            Ticket ticket = reservationService.saveTicket(Ticket.makeTicketOf(reservation, concert, performance, seat));
            tickets.add(ticket);
        }

        return new ReservationResponse(reservation, tickets);
    }


    /**
     * 결제 요청
     * @param paymentRequest 결제 요청 정보
     * @return 결제 요청 결과
     */
    @Transactional(noRollbackFor = { TimeOutCustomException.class, InsufficientResourcesCustomException.class })
    public PaymentResponse requestPayment(PaymentRequest paymentRequest) {
        // request body 확인
        if (paymentRequest == null) {
            throw new IllegalArgumentCustomException("결제 요청 정보가 없습니다.", ResponseMessage.NOT_FOUND);
        }
        // 고객 ID
        if (!Customer.isAvailableCustomerId(paymentRequest.getCustomerId())) {
            throw new IllegalArgumentCustomException("고객 ID 값이 잘못 되었습니다.", ResponseMessage.INVALID);
        }
        // 예약 ID
        if (!Reservation.isAvailableReservationId(paymentRequest.getReservationId())) {
            throw new IllegalArgumentCustomException("예약 ID 값이 잘못 되었습니다.", ResponseMessage.NOT_FOUND);
        }
        // 결제 정보 확인
        if (paymentRequest.getPayInfos() == null || paymentRequest.getPayInfos().isEmpty()) {
            throw new IllegalArgumentCustomException("결제 정보가 없습니다.", ResponseMessage.NOT_FOUND);
        }
        boolean isValid;
        isValid = paymentRequest.getPayInfos()
                .stream()
                .anyMatch(payInfo -> Payment.isAvailablePayMethod(payInfo.getPayMethod())
                                    && Payment.isAvailablePayAmount(payInfo.getPayAmount()));
        if (!isValid) {
            throw new IllegalArgumentCustomException("결제 정보가 유효하지 않습니다.", ResponseMessage.INVALID);
        }

        // 고객 확인
        Customer customer;
        boolean isAvailable;

        // 고객 조회
        customer = customerService.getAvailableCustomer(paymentRequest.getCustomerId());
        // 고객 검증
        isAvailable = customerService.meetsIfCustomerValid(customer);
        if (!isAvailable) {
            throw new ResourceNotFoundCustomException("존재하지 않는 고객입니다.", ResponseMessage.NOT_AVAILABLE);
        }

        // 예약 정보 확인
        Reservation reservation;
        boolean isRequested;
        boolean isValidDuration;
        // 예약 조회
        reservation = reservationService.getReservation(paymentRequest.getReservationId(), paymentRequest.getCustomerId());
        // 예약 유효성 검증
        isAvailable = reservationService.meetsIfReservationAvailable(reservation);
        if (!isAvailable) {
            throw new ResourceNotFoundCustomException("존재하지 않는 예약 정보입니다.", ResponseMessage.NOT_AVAILABLE);
        }
        // 예약 상태 확인
        isRequested = Reservation.isAbleToPayReservationStatus(reservation.reservationStatus());
        if (!isRequested) {
            throw new IllegalStateCustomException("결제가 불가능한 예약 상태입니다.", ResponseMessage.INVALID);
        }
        // 예약 상태 변경 일시로 결제 가능 기간 확인 (5분)
        isValidDuration = reservationService.meetsIfAbleToPayInTimeLimits(300L, reservation.reservationStatusChangedAt());
        if (!isValidDuration) {
            // 예약 및 티켓 취소 처리, 좌석 상태 변경
            reservationService.updateReservationStatus(paymentRequest.getReservationId(), paymentRequest.getCustomerId(), ReservationStatus.CANCELLED);
            reservationService.updateTicketsStatus(paymentRequest.getReservationId(), TicketStatus.PAY_WAITING, TicketStatus.CANCELLED_AFTER_PAY_WAITING);
            seatService.updateSeatStatusByReservationIdAndReservationStatus(paymentRequest.getReservationId(), SeatStatus.WAITING_FOR_RESERVATION, SeatStatus.AVAILABLE);

            throw new TimeOutCustomException("결제 가능 시간을 초과하였습니다.", ResponseMessage.OUT_OF_TIME);
        }

        // 결제금액 == 결제 요청 중인 티켓 총액 확인
        BigDecimal sumOfPayInfoPayAmount;
        boolean isEqualAmount;

        // 결제금액 총액
        sumOfPayInfoPayAmount = paymentRequest.getPayInfos()
                .stream()
                .map(PaymentRequest.PayInfo::getPayAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // 결제금액 검증 - client 에서 받은 금액 과 티켓 금액 합해서 확인
        isEqualAmount = paymentService.meetsIfSumOfPayAmountEqualsToSumOfTicketsPrice(sumOfPayInfoPayAmount, reservationService.getSumOfTicketPrice(reservation.reservationId(), TicketStatus.PAY_WAITING));

        // 결제 방식으로 검증 - 포인트, (카드, 무통장입금은 아직 미구현)
        boolean isPointAffordable = paymentRequest.getPayInfos()
                .stream()
                .filter(payInfo -> payInfo.getPayMethod().equals(PayMethod.POINT))
                .findAny()
                .map(payInfo -> customerService.meetsIfPointBalanceSufficient(paymentRequest.getCustomerId(), payInfo.getPayAmount()))
                .orElse(false);

        if (!isEqualAmount || !isPointAffordable) {
            // 예약 및 티켓 취소 처리, 좌석 상태 변경
            reservationService.updateReservationStatus(paymentRequest.getReservationId(), paymentRequest.getCustomerId(), ReservationStatus.CANCELLED);
            reservationService.updateTicketsStatus(paymentRequest.getReservationId(), TicketStatus.PAY_WAITING, TicketStatus.CANCELLED_AFTER_PAY_WAITING);
            seatService.updateSeatStatusByReservationIdAndReservationStatus(paymentRequest.getReservationId(), SeatStatus.WAITING_FOR_RESERVATION, SeatStatus.AVAILABLE);

            if (!isEqualAmount) throw new InsufficientResourcesCustomException("결제금액이 맞지 않습니다.", ResponseMessage.INVALID);
            throw new InsufficientResourcesCustomException("포인트 잔액이 부족합니다.", ResponseMessage.OUT_OF_BUDGET);
        }

        // 포인트 차감
        BigDecimal pointUseAmount = paymentRequest.getPayInfos()
                .stream()
                .filter(payInfo -> payInfo.getPayMethod().equals(PayMethod.POINT))
                .findAny()
                .map(PaymentRequest.PayInfo::getPayAmount)
                .orElse(BigDecimal.ZERO);
        customerService.useCustomerPoint(paymentRequest.getCustomerId(), pointUseAmount);

        // 결제 정보 생성
        Payment payment = paymentService.savePayment(Payment.create(null, reservation.reservationId(), PayMethod.POINT, pointUseAmount, pointUseAmount, BigDecimal.ZERO));

        // 예약 및 티켓 상태 변경, 좌석 상태 변경
        Reservation changedReservation = reservationService.updateReservationStatus(paymentRequest.getReservationId(), paymentRequest.getCustomerId(), ReservationStatus.COMPLETED);
        List<Ticket> changedTickets = reservationService.updateTicketsStatus(paymentRequest.getReservationId(), TicketStatus.PAY_WAITING, TicketStatus.PAYED);
        seatService.updateSeatStatusByReservationIdAndReservationStatus(paymentRequest.getReservationId(), SeatStatus.WAITING_FOR_RESERVATION ,SeatStatus.TAKEN);

        return new PaymentResponse(changedReservation, changedTickets, List.of(payment));
    }

    /**
     * 결제 요청 시간이 만료된 예약의 좌석 임시 배정 취소
     */
    public void removeWaitingReservation() {
        Date now = new Date();

        List<Reservation> reservations = reservationService.getAllReservationByReservationStatus(ReservationStatus.REQUESTED);

        for (Reservation reservation : reservations) {
            Date statusChangedAt = reservation.reservationStatusChangedAt();
            long duration = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - statusChangedAt.getTime());

            if (duration > 300) {
                // 예약 취소
                reservationService.updateReservationStatus(reservation.reservationId(), reservation.customerId(), ReservationStatus.CANCELLED);
                // 티켓 취소
                reservationService.updateTicketsStatus(reservation.reservationId(), TicketStatus.PAY_WAITING, TicketStatus.CANCELLED_AFTER_PAY_WAITING);
                // 좌석 배정 취소
                seatService.updateSeatStatusByReservationIdAndReservationStatus(reservation.reservationId(), SeatStatus.WAITING_FOR_RESERVATION, SeatStatus.AVAILABLE);
            }
        }
    }
}
