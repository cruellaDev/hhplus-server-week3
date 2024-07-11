package com.io.hhplus.concert.domain.reservation.service;

import com.io.hhplus.concert.common.enums.ReservationStatus;
import com.io.hhplus.concert.common.enums.TicketStatus;
import com.io.hhplus.concert.domain.reservation.model.Reservation;
import com.io.hhplus.concert.domain.reservation.model.Ticket;
import com.io.hhplus.concert.domain.reservation.repository.ReservationRepository;
import com.io.hhplus.concert.domain.reservation.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TicketRepository ticketRepository;

    /**
     * 예약 저장
     * @param reservation 예약 정보
     * @return 예약 저장 정보
     */
    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    /**
     * 티켓 저장
     * @param ticket 티켓 정보
     * @return 티켓 저장 정보
     */
    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    /**
     * 예약 조회
     * @param reservationId 예약_ID
     * @param customerId 고객_ID
     * @return 예약 정보
     */
    public Reservation getReservation(Long reservationId, Long customerId) {
        return reservationRepository.findByIdAndCustomerId(reservationId, customerId).orElseGet(Reservation::noContents);
    }

    /**
     * 예약 유효성 검증
     * @param reservation 예약 정보
     * @return 예약 유효 여부
     */
    public boolean meetsIfReservationAvailable(Reservation reservation) {
        return reservation != null
                && Reservation.isAvailableReservationId(reservation.reservationId())
                && Reservation.isAvailableReserverName(reservation.reserverName())
                && Reservation.isAvailableReceiveMethod(reservation.receiveMethod())
                && Reservation.isAvailableReceiverName(reservation.receiverName())
                && Reservation.isAvailableReservationStatus(reservation.reservationStatus());
    }

    /**
     * 예약 정보 결제 유효 기간 내 존재 여부
     * @param seconds 유효 기준 기간 단위 (초)
     * @param reservationStatusChangedAt 예약상태변경일시
     * @return 예약 정보 결제 유효 기간 내 존재 여부 (예약상태변경일시와 현재일시의 차가 유효 기준 기간 단위 이내 이면 true)
     */
    public boolean meetsIfAbleToPayInTimeLimits(Long seconds, Date reservationStatusChangedAt) {
        Date currentDate = new Date();
        long targetSeconds = TimeUnit.MILLISECONDS.toSeconds(currentDate.getTime() - reservationStatusChangedAt.getTime());
        return Reservation.isInPayableDuration(seconds, targetSeconds);
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
    public Reservation updateReservationStatus(Long reservationId, Long customerId, ReservationStatus reservationStatus) {
        Reservation reservation = reservationRepository.findByIdAndCustomerId(reservationId, customerId).orElseGet(Reservation::noContents);
        return reservationRepository.save(Reservation.changeStatus(reservation, reservationStatus));
    }

    /**
     * 티켓 상태 변경
     * @param reservationId 예약 ID
     * @param asisTicketStatus 이전 티켓상태
     * @param tobeTicketStatus 이후 티켓상태
     * @return 변경된 티켓
     */
    public List<Ticket> updateTicketsStatus(Long reservationId, TicketStatus asisTicketStatus, TicketStatus tobeTicketStatus) {
        List<Ticket> asisTickets = ticketRepository.findAllByReservationIdAndTicketStatus(reservationId, asisTicketStatus);
        List<Ticket> tobeTickets = new ArrayList<>();
        for (Ticket asisTicket : asisTickets) {
            if (tobeTicketStatus.equals(TicketStatus.PAYED)) {
                Ticket tobeTicket = Ticket.changeByTicketStatus(asisTicket, tobeTicketStatus);
                tobeTickets.add(tobeTicket);
            }
        }
        return ticketRepository.saveAll(tobeTickets);
    }

    /**
     * 예약 상태값으로 모든 예약 조회
     * @param reservationStatus 예약 상태
     * @return 예약 목록
     */
    public List<Reservation> getAllReservationByReservationStatus(ReservationStatus reservationStatus) {
        return reservationRepository.findAllByReservationStatus(reservationStatus);
    }

}
