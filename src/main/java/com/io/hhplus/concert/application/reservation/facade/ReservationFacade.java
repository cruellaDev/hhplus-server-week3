package com.io.hhplus.concert.application.reservation.facade;

import com.io.hhplus.concert.application.reservation.dto.*;
import com.io.hhplus.concert.common.enums.*;
import com.io.hhplus.concert.common.exceptions.*;
import com.io.hhplus.concert.common.utils.DateUtils;
import com.io.hhplus.concert.domain.concert.service.ConcertValidator;
import com.io.hhplus.concert.domain.concert.service.model.ConcertModel;
import com.io.hhplus.concert.domain.concert.service.model.PerformanceModel;
import com.io.hhplus.concert.domain.concert.service.model.SeatModel;
import com.io.hhplus.concert.domain.concert.service.ConcertService;
import com.io.hhplus.concert.domain.customer.service.CustomerValidator;
import com.io.hhplus.concert.domain.customer.service.model.CustomerModel;
import com.io.hhplus.concert.domain.customer.service.CustomerService;
import com.io.hhplus.concert.domain.reservation.service.PaymentValidator;
import com.io.hhplus.concert.domain.reservation.service.ReservationValidator;
import com.io.hhplus.concert.domain.reservation.service.model.PaymentModel;
import com.io.hhplus.concert.domain.reservation.service.model.ReservationModel;
import com.io.hhplus.concert.domain.reservation.service.model.TicketModel;
import com.io.hhplus.concert.domain.reservation.service.PaymentService;
import com.io.hhplus.concert.domain.reservation.service.ReservationService;
import com.io.hhplus.concert.domain.waiting.service.WaitingValidator;
import com.io.hhplus.concert.domain.waiting.service.model.WaitingQueueModel;
import com.io.hhplus.concert.domain.waiting.service.WaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationFacade {

    private final CustomerService customerService;
    private final WaitingService waitingService;
    private final ConcertService concertService;
    private final ReservationService reservationService;
    private final PaymentService paymentService;

    private final ConcertValidator concertValidator;
    private final CustomerValidator customerValidator;
    private final WaitingValidator waitingValidator;
    private final PaymentValidator paymentValidator;
    private final ReservationValidator reservationValidator;

    /**
     * 좌석 예약 요청
     * @param customerId 고객_ID
     * @param reserverInfo 예약 정보
     * @param ticketInfo 예매 정보
     * @return 예약 요청 결과
     */
    @Transactional
    public ReservationResultInfoWithTickets requestReservation(Long customerId, ReserverInfo reserverInfo, TicketInfo ticketInfo) {
        // 고객 확인
        CustomerModel customerModel;
        // 고객 조회
        customerModel = customerService.getAvailableCustomer(customerId);
        // 고객 검증
        customerValidator.checkIfCustomerValid(customerModel);

        // 대기열 토큰 확인
        WaitingQueueModel waitingQueueModel;
        boolean isExists;
        boolean isActive;
        // 대기열 조회
        waitingQueueModel = waitingService.getActiveWaitingTokenByCustomerId(customerId);

        // 대기열 존재 여부 확인
        isExists = waitingValidator.meetsIfActiveWaitingQueueExists(waitingQueueModel);

        // 토큰 활성 시간 확인
        isActive = waitingValidator.meetsIfActiveWaitingQueueInTimeLimits(300L, waitingQueueModel.createdAt());
        // 유효 대기열 없을 시
        if (!isExists && !isActive) {
            throw new CustomException(ResponseMessage.WAITING_NOT_FOUND, "대기열 진입 정보가 존재하지 않습니다.");
        }

        // 콘서트 확인
        ConcertModel concertModel;
        // 콘서트 조회
        concertModel = concertService.getAvailableConcert(ticketInfo.getConcertId());
        // 콘서트 검증
        concertValidator.checkIfConcertToBeReserved(concertModel);

        // 공연 확인
        PerformanceModel performanceModel;
        // 공연 조회
        performanceModel = concertService.getAvailablePerformance(ticketInfo.getConcertId(), ticketInfo.getPerformanceId());
        // 공연 검증
        concertValidator.checkIfPerformanceToBeReserved(performanceModel, DateUtils.getSysDate());

        // 좌석 확인
        List<SeatModel> seatModels;
        // 좌석 조회 후 임시 배정
        seatModels = ticketInfo.getSeatIds()
                .stream()
                .map(seatId -> {
                    SeatModel seatModel = concertService.getAvailableSeat(ticketInfo.getPerformanceId(), seatId);
                    if (concertValidator.isTaken(seatModel.seatStatus()) || concertValidator.isOccupied(seatModel.seatStatus())) {
                        throw new CustomException(ResponseMessage.CONCERT_NOT_AVAILABLE, "이미 선점된 좌석입니다.");
                    }
                    concertValidator.checkIfSeatToBeReserved(seatModel);
                    concertValidator.checkIfSeatNotAssigned(seatModel.seatId());
                    return concertService.assignSeatTemporarily(seatModel.seatId());
                }).toList();

        // 토큰 대기 만료 처리
        boolean isExpired;
        isExpired = waitingService.expireWaitingQueueToken(waitingQueueModel);
        if (!isExpired) {
            throw new CustomException(ResponseMessage.FAIL, "오류가 발생하였습니다 [대기열 토큰]");
        }

        // 예약 및 티켓 생성
        ReservationModel reservationModel;
        List<TicketModel> ticketModels;
        // 예약 저장
        reservationModel = reservationService.saveReservation(ReservationModel.create(
                null,
                customerId,
                reserverInfo.getReserverName(),
                ReservationStatus.REQUESTED,
                new Date(),
                reserverInfo.getReceiveMethod(),
                reserverInfo.getReceiverName(),
                reserverInfo.getReceiverPostcode(),
                reserverInfo.getReceiverBaseAddress(),
                reserverInfo.getReceiverDetailAddress()
        ));
        // 티켓 저장
        ticketModels = seatModels
                .stream()
                .map(seatModel -> reservationService.saveTicket(TicketModel.makeTicketOf(reservationModel, concertModel, performanceModel, seatModel)))
                .toList();

        return new ReservationResultInfoWithTickets(reservationModel, ticketModels);
    }

    /**
     * 결제 요청
     * @param customerId 고객_ID
     * @param reservationId 예약_ID
     * @param paymentInfos 결제 정보
     * @return 결제 요청 결과
     */
    @Transactional(noRollbackFor = { TimeOutCustomException.class, InsufficientResourcesCustomException.class })
    public PaymentResultInfoWithReservationAndTickets requestPayment(Long customerId, Long reservationId, List<PaymentInfo> paymentInfos) {
        if (customerId == null) throw new CustomException(ResponseMessage.INVALID, "고객 ID가 존재하지 않습니다.");
        if (reservationId == null) throw new CustomException(ResponseMessage.INVALID, "예약 ID가 존재하지 않습니다.");
        if (paymentInfos == null || paymentInfos.isEmpty()) throw new CustomException(ResponseMessage.INVALID, "결제 정보가 존재하지 않습니다.");

        // 고객 확인
        CustomerModel customerModel;
        // 고객 조회
        customerModel = customerService.getAvailableCustomer(customerId);
        // 고객 검증
        customerValidator.checkIfCustomerValid(customerModel);

        // 예약 정보 확인
        ReservationModel reservationModel;
        boolean isValidDuration;
        // 예약 조회
        reservationModel = reservationService.getReservation(reservationId, customerId);
        // 예약 유효성 검증
        if (!reservationValidator.meetsIfReservationAvailable(reservationModel)) {
            throw new CustomException(ResponseMessage.RESERVATION_NOT_FOUND, "존재하지 않는 예약 정보입니다.");
        }
        // 예약 상태 확인
        if (reservationValidator.isNotAbleToPayReservationStatus(reservationModel.reservationStatus())) {
            throw new CustomException(ResponseMessage.RESERVATION_NOT_AVAILABLE, "결제가 불가능한 예약 상태입니다.");
        }
        // 예약 상태 변경 일시로 결제 가능 기간 확인 (5분)
        isValidDuration = reservationValidator.meetsIfAbleToPayInTimeLimits(300L, reservationModel.reservationStatusChangedAt());
        if (!isValidDuration) {
            // 예약 및 티켓 취소 처리, 좌석 상태 변경
            reservationService.updateReservationStatus(reservationId, customerId, ReservationStatus.CANCELLED);
            reservationService.updateTicketsStatus(reservationId, TicketStatus.PAY_WAITING, TicketStatus.CANCELLED_AFTER_PAY_WAITING);
            concertService.updateSeatStatusByReservationIdAndReservationStatus(reservationId, SeatStatus.WAITING_FOR_RESERVATION, SeatStatus.AVAILABLE);

            throw new TimeOutCustomException(ResponseMessage.PAYMENT_OUT_OF_TIME, "결제 가능 시간을 초과하였습니다.");
        }

        // 결제금액 == 결제 요청 중인 티켓 총액 확인
        BigDecimal sumOfPayInfoPayAmount;
        boolean isEqualAmount;

        // 결제금액 총액
        sumOfPayInfoPayAmount = paymentInfos
                .stream()
                .map(PaymentInfo::getPayAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // 결제금액 검증 - client 에서 받은 금액 과 티켓 금액 합해서 확인
        isEqualAmount = paymentValidator.meetsIfSumOfPayAmountEqualsToSumOfTicketsPrice(sumOfPayInfoPayAmount, reservationService.getSumOfTicketPrice(reservationModel.reservationId(), TicketStatus.PAY_WAITING));

        // 결제 방식으로 검증 - 포인트, (카드, 무통장입금은 아직 미구현)
        boolean isSufficientPointBalance = paymentInfos
                .stream()
                .filter(payInfo -> payInfo.getPayMethod().equals(PayMethod.POINT))
                .map(payInfo -> customerValidator.meetsIfPointBalanceSufficient(customerId, payInfo.getPayAmount()))
                .findAny()
                .orElse(false);

        if (!isEqualAmount || !isSufficientPointBalance) {
            // 예약 및 티켓 취소 처리, 좌석 상태 변경
            reservationService.updateReservationStatus(reservationId, customerId, ReservationStatus.CANCELLED);
            reservationService.updateTicketsStatus(reservationId, TicketStatus.PAY_WAITING, TicketStatus.CANCELLED_AFTER_PAY_WAITING);
            concertService.updateSeatStatusByReservationIdAndReservationStatus(customerId, SeatStatus.WAITING_FOR_RESERVATION, SeatStatus.AVAILABLE);

            if (!isEqualAmount) throw new InsufficientResourcesCustomException(ResponseMessage.INVALID, "결제금액이 맞지 않습니다.");
            throw new InsufficientResourcesCustomException(ResponseMessage.OUT_OF_BUDGET, "포인트 잔액이 부족합니다.");
        }

        // 포인트 차감
        BigDecimal pointUseAmount = paymentInfos
                .stream()
                .filter(payInfo -> payInfo.getPayMethod().equals(PayMethod.POINT))
                .findAny()
                .map(PaymentInfo::getPayAmount)
                .orElse(BigDecimal.ZERO);
        customerService.useCustomerPoint(customerId, pointUseAmount);

        // 결제 정보 생성
        PaymentModel paymentModel = paymentService.savePayment(PaymentModel.create(null, reservationModel.reservationId(), PayMethod.POINT, pointUseAmount, pointUseAmount, BigDecimal.ZERO));

        // 예약 및 티켓 상태 변경, 좌석 상태 변경
        ReservationModel changedReservationModel = reservationService.updateReservationStatus(reservationId, customerId, ReservationStatus.COMPLETED);
        List<TicketModel> changedTicketModels = reservationService.updateTicketsStatus(reservationId, TicketStatus.PAY_WAITING, TicketStatus.PAYED);
        concertService.updateSeatStatusByReservationIdAndReservationStatus(reservationId, SeatStatus.WAITING_FOR_RESERVATION ,SeatStatus.TAKEN);

        return new PaymentResultInfoWithReservationAndTickets(changedReservationModel, changedTicketModels, List.of(paymentModel));
    }

    /**
     * 결제 요청 시간이 만료된 예약의 좌석 임시 배정 취소
     */
    public void removeWaitingReservation() {
        Date currentDate = DateUtils.getSysDate();
        long durationLimit = 300;

        List<ReservationModel> reservationModels = reservationService.getAllReservationByReservationStatus(ReservationStatus.REQUESTED);

        reservationModels.forEach(reservationModel -> {
            Date statusChangedAt = reservationModel.reservationStatusChangedAt();
            long duration = DateUtils.calculateDuration(currentDate, statusChangedAt);
            if (duration > durationLimit) {
                // 예약 취소
                reservationService.updateReservationStatus(reservationModel.reservationId(), reservationModel.customerId(), ReservationStatus.CANCELLED);
                // 티켓 취소
                reservationService.updateTicketsStatus(reservationModel.reservationId(), TicketStatus.PAY_WAITING, TicketStatus.CANCELLED_AFTER_PAY_WAITING);
                // 좌석 배정 취소
                concertService.updateSeatStatusByReservationIdAndReservationStatus(reservationModel.reservationId(), SeatStatus.WAITING_FOR_RESERVATION, SeatStatus.AVAILABLE);
            }
        });
    }
}
