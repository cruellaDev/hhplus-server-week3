package com.io.hhplus.concert.domain.concert;

import com.io.hhplus.concert.common.GlobalConstants;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.common.utils.DateUtils;
import com.io.hhplus.concert.domain.concert.dto.AvailableSeatInfo;
import com.io.hhplus.concert.domain.concert.dto.ReservationInfo;
import com.io.hhplus.concert.domain.concert.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;


@Slf4j
@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;

    /**
     * 콘서트 등록
     * @param command 콘서트 등록 command
     * @return 저장된 콘서트 정보
     */
    public Concert registerConcert(ConcertCommand.RegisterConcertCommand command) {
        return concertRepository.saveConcert(Concert.create().register(command));
    }

    /**
     * 콘서트 목록 조회
     * @return 콘서트 목록
     */
    public List<Concert> getConcerts() {
        return concertRepository.findConcerts()
                .stream()
                .filter(Concert::isNotDeleted)
                .toList();
    }

    /**
     * 콘서트 일정 등록
     * @param command 콘서트 일정 등록 command
     * @return 저정된 콘서트 일정 정보
     */
    public ConcertSchedule registerConcertSchedule(ConcertCommand.RegisterConcertScheduleCommand command) {
        return concertRepository.saveConcertSchedule(ConcertSchedule.create().register(command));
    }

    /**
     * 현재일시 기준 특정 콘서트의 예약 가능 일정 목록 조회
     * @param concertId 콘서트 ID
     * @return 현재일시 기준 특정 콘서트의 예약가능 일정 목록 조회
     */
    public List<ConcertSchedule> getAvailableSchedules(Long concertId) {
        log.info("[service-concert-getAvailableSchedules] - concertId : {}", concertId);
        return concertRepository.findConcertSchedules(concertId)
                .stream()
                .filter(concertSchedule
                        -> concertSchedule.isToBePerformed()
                        && concertSchedule.isNotDeleted())
                .toList();
    }

    /**
     * 콘서트 좌석 등록
     * @param command 콘서트 좌석 등록 command
     * @return 저장된 콘서트 좌석 정보
     */
    public ConcertSeat registerConcertSeat(ConcertCommand.RegisterConcertSeatCommand command) {
        return concertRepository.saveConcertSeat(ConcertSeat.create().register(command));
    }

    /**
     * 특정 콘서트의 공연 구역 예약 가능 좌석 목록 조회
     * @param concertId 콘서트_ID
     * @param concertScheduleId 콘서트_일정_ID
     * @return 예약 가능 좌석 목록
     */
    public List<AvailableSeatInfo> getAvailableSeats(Long concertId, Long concertScheduleId) {
        ConcertSeat seat = concertRepository.findConcertSeat(concertId, concertScheduleId).orElseThrow(() -> new CustomException(ResponseMessage.CONCERT_SEAT_NOT_FOUND));
        log.info("[service-concert-get-getAvailableSeats] - concertId : {}, concertScheduleId : {}", concertId, concertScheduleId);
        long lStart = 1;
        long lEnd = seat.seatCapacity();
        return LongStream.rangeClosed(lStart, lEnd)
                .filter(seatSequence -> concertRepository.findOccupiedSeatsFromTicket(seat.concertId(), seat.concertScheduleId(), String.valueOf(seatSequence)).isEmpty())
                .mapToObj(seatSequence -> AvailableSeatInfo.of(seat, seatSequence))
                .toList();
    }

    /**
     * 좌석 배정 및 예약 요청
     * @param command 좌석 예약 command
     * @return 응답 정보
     */
    @Transactional
    public ReservationInfo reserveSeats(ConcertCommand.ReserveSeatsCommand command) {
        // 생성된 예약과 티켓이 있는지 확인 후 결제 생성 진행
        // 동일 유저 접근
        boolean isReserved = concertRepository.findReservationAlreadyExists(command.getCustomerId(), command.getConcertId(), command.getConcertScheduleId(), command.getSeatNumbers()).isPresent();
        if (isReserved) throw new CustomException(ResponseMessage.ALREADY_RESERVED);

        // 다른 유저 접근
        boolean isReservationExists = !concertRepository.findReservationsAlreadyExists(command.getConcertId(), command.getConcertScheduleId(), command.getSeatNumbers()).isEmpty();
        if (isReservationExists) throw new CustomException(ResponseMessage.SEAT_TAKEN);

        // 콘서트 정보 조회
        Concert concert = concertRepository.findConcert(command.getConcertId())
                .filter(o
                        -> o.isAvailableConcertStatus()
                        && o.isAbleToBook()
                        && o.isNotDeleted())
                .orElseThrow(() -> new CustomException(ResponseMessage.CONCERT_NOT_FOUND));
        ConcertSchedule concertSchedule = concertRepository.findConcertSchedule(command.getConcertId(), command.getConcertScheduleId())
                .filter(o
                        -> o.isToBePerformed()
                        && o.isNotDeleted())
                .orElseThrow(() -> new CustomException(ResponseMessage.CONCERT_SCHEDULE_NOT_FOUND));
        ConcertSeat concertSeat = concertRepository.findConcertSeat(command.getConcertId(), command.getConcertScheduleId())
                .filter(ConcertSeat::isNotDeleted)
                .orElseThrow(() -> new CustomException(ResponseMessage.CONCERT_SEAT_NOT_FOUND));

        // 좌석 확인
        boolean isSeatTaken = command.getSeatNumbers()
                .stream()
                .anyMatch(seatNumber -> !(concertRepository.findOccupiedSeatsFromTicket(command.getConcertId(), command.getConcertScheduleId(), seatNumber).isEmpty()));
        if (isSeatTaken) throw new CustomException(ResponseMessage.SEAT_TAKEN);

        Reservation reservation = concertRepository.saveReservation(Reservation.create().reserve(command));
        List<Ticket> tickets = command.getSeatNumbers()
                .stream()
                .map(seatNumber -> concertRepository.saveTicket(Ticket.reserve(reservation, concert, concertSchedule, concertSeat, seatNumber)))
                .toList();

        return ReservationInfo.of(reservation, tickets);
    }

    /**
     * 예약 완료
     */
    @Transactional
    public ReservationInfo confirmReservation(ConcertCommand.ConfirmReservationCommand command) {
        // 예약 조회
        Reservation reservation = concertRepository.findReservation(command.getReservationId(), command.getCustomerId())
                .filter(o
                        -> o.isAbleToPay()
                        && o.isNotDeleted())
                .orElseThrow(() -> new CustomException(ResponseMessage.RESERVATION_NOT_FOUND));
        // 티켓
        List<Ticket> tickets = concertRepository.findTickets(command.getReservationId())
                .stream()
                .map(ticket -> ticket.confirmReservation(reservation))
                .toList();
        if (tickets.isEmpty()) throw new CustomException(ResponseMessage.TICKET_NOT_FOUND);

        return ReservationInfo.of(
                reservation,
                tickets.stream().map(concertRepository::saveTicket).collect(Collectors.toList())
        );
    }
}
