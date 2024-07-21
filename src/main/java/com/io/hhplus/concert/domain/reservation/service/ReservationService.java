package com.io.hhplus.concert.domain.reservation.service;

import com.io.hhplus.concert.common.enums.ReservationStatus;
import com.io.hhplus.concert.common.enums.TicketStatus;
import com.io.hhplus.concert.domain.reservation.service.model.ReservationModel;
import com.io.hhplus.concert.domain.reservation.service.model.TicketModel;
import com.io.hhplus.concert.domain.reservation.repository.ReservationRepository;
import com.io.hhplus.concert.domain.reservation.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TicketRepository ticketRepository;

    /**
     * 예약 저장
     * @param reservationModel 예약 정보
     * @return 예약 저장 정보
     */
    public ReservationModel saveReservation(ReservationModel reservationModel) {
        return reservationRepository.save(reservationModel);
    }

    /**
     * 티켓 저장
     * @param ticketModel 티켓 정보
     * @return 티켓 저장 정보
     */
    public TicketModel saveTicket(TicketModel ticketModel) {
        return ticketRepository.save(ticketModel);
    }

    /**
     * 예약 조회
     * @param reservationId 예약_ID
     * @param customerId 고객_ID
     * @return 예약 정보
     */
    public ReservationModel getReservation(Long reservationId, Long customerId) {
        return reservationRepository.findByIdAndCustomerId(reservationId, customerId).orElseGet(ReservationModel::noContents);
    }

    /**
     * 티켓 가격 합계 조회
     * @param reservationId 예약_ID
     * @param ticketStatus 티켓 상태
     * @return 티켓 가격 합계
     */
    public BigDecimal getSumOfTicketPrice(Long reservationId, TicketStatus ticketStatus) {
        return ticketRepository.sumTicketPriceByReservationIdAndTicketStatus(reservationId, ticketStatus);
    }

    /**
     * 예약 상태 변경
     * @param reservationId 예약_ID
     * @param reservationStatus 예약 상태
     * @return 변경된 예약 상태 정보
     */
    public ReservationModel updateReservationStatus(Long reservationId, Long customerId, ReservationStatus reservationStatus) {
        ReservationModel reservationModel = reservationRepository.findByIdAndCustomerId(reservationId, customerId).orElseGet(ReservationModel::noContents);
        return reservationRepository.save(ReservationModel.changeStatus(reservationModel, reservationStatus));
    }

    /**
     * 티켓 상태 변경
     * @param reservationId 예약 ID
     * @param asisTicketStatus 이전 티켓상태
     * @param tobeTicketStatus 이후 티켓상태
     * @return 변경된 티켓
     */
    public List<TicketModel> updateTicketsStatus(Long reservationId, TicketStatus asisTicketStatus, TicketStatus tobeTicketStatus) {
        List<TicketModel> asisTicketModels = ticketRepository.findAllByReservationIdAndTicketStatus(reservationId, asisTicketStatus);
        List<TicketModel> tobeTicketModels = asisTicketModels
                .stream()
                .map(asisTicketModel -> TicketModel.changeByTicketStatus(asisTicketModel, tobeTicketStatus))
                .collect(Collectors.toList());
        return ticketRepository.saveAll(tobeTicketModels);
    }

    /**
     * 예약 상태값으로 모든 예약 조회
     * @param reservationStatus 예약 상태
     * @return 예약 목록
     */
    public List<ReservationModel> getAllReservationByReservationStatus(ReservationStatus reservationStatus) {
        return reservationRepository.findAllByReservationStatus(reservationStatus);
    }

}
