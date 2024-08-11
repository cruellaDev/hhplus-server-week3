package com.io.hhplus.concert.domain.concert;

import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.domain.concert.dto.AvailableSeatInfo;
import com.io.hhplus.concert.domain.concert.dto.ReservationInfo;
import com.io.hhplus.concert.domain.concert.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public List<Concert> getAvailableConcerts() {
        return concertRepository.findConcerts();
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
        return concertRepository.findConcertSchedules(concertId);
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
        // 콘서트 정보 조회
        Concert concert = concertRepository.findConcert(command.getConcertId()).orElseThrow(() -> new CustomException(ResponseMessage.CONCERT_NOT_FOUND));
        concert.checkValid();
        // 콘서트 일정 정보 조회
        ConcertSchedule concertSchedule = concertRepository.findConcertSchedule(command.getConcertId(), command.getConcertScheduleId()).orElseThrow(() -> new CustomException(ResponseMessage.CONCERT_SCHEDULE_NOT_FOUND));
        concertSchedule.checkValid();
        // 콘서트 좌석 정보 조회
        // TODO Lock
        ConcertSeat concertSeat = concertRepository.findConcertSeat(command.getConcertId(), command.getConcertScheduleId()).orElseThrow(() -> new CustomException(ResponseMessage.CONCERT_SEAT_NOT_FOUND));
        concertSeat.checkValid();

        // 좌석확인
        boolean isSeatTaken = !concertRepository.findReservationsAlreadyExists(command.getConcertId(), command.getConcertScheduleId(), command.getSeatNumbers()).isEmpty();
        if (isSeatTaken) throw new CustomException(ResponseMessage.SEAT_TAKEN);

        Reservation reservation = Reservation
                .reserve(command.getCustomerId(), command.getBookerName())
                .addNewTickets(concert, concertSchedule, concertSeat, command.getSeatNumbers());

        return ReservationInfo.from(concertRepository.saveReservation(reservation));
    }

    /**
     * 예약 완료
     */
    @Transactional
    public ReservationInfo confirmReservation(ConcertCommand.ConfirmReservationCommand command) {
        // 예약 조회
        Reservation reservation = concertRepository.findReservation(command.getReservationId(), command.getCustomerId()).orElseThrow(() -> new CustomException(ResponseMessage.RESERVATION_NOT_FOUND));
        // 티켓 조회
        List<Ticket> tickets = concertRepository.findTickets(command.getReservationId());

        reservation.addConfirmedTickets(tickets);

        // TODO 예약 상태 추가
        return ReservationInfo.from(concertRepository.saveReservation(reservation));
    }
}
