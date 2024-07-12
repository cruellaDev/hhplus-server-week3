package com.io.hhplus.concert.domain.concert.service;

import com.io.hhplus.concert.common.enums.SeatStatus;
import com.io.hhplus.concert.domain.concert.model.Seat;
import com.io.hhplus.concert.domain.concert.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

    /**
     * 예약 가능 콘서트 공연 좌석 목록 조회
     * @param performanceId 공연_ID
     * @return 예약 가능 좌석 목록
     */
    public List<Seat> getAvailableSeats(Long performanceId) {
        return seatRepository.findAvailableAllByPerformanceId(performanceId);
    }

    /**
     * 예약 가능 콘서트 공연 좌석 단건 조회
     * @param performanceId 공연_ID
     * @param seatId 좌석_ID
     * @return 예약 가능 좌석 정보
     */
    public Seat getAvailableSeat(Long performanceId, Long seatId) {
        return seatRepository.findAvailableOneByPerformanceIdAndSeatId(performanceId, seatId).orElseGet(Seat::noContents);
    }

    /**
     * 좌석 예약 가능 여부 확인
     * @param seat 좌석 정보
     * @return 예약 가능 여부
     */
    public boolean meetsIfSeatToBeReserved(Seat seat) {
        return seat != null
                && Seat.isAvailableSeatId(seat.seatId())
                && Seat.isAvailablePerformanceId(seat.performanceId())
                && Seat.isAvailableConcertId(seat.concertId())
                && Seat.isAvailableSeatNo(seat.seatNo())
                && Seat.isAvailableSeatStatus(seat.seatStatus())
                ;
    }

    /**
     * 좌석 임시 배정
     * @param seatId 좌석_ID
     * @return 좌석 배정 여부
     */
    public boolean assignSeatTemporarily(Long seatId) {
        boolean isAssigned = true;
        if (seatId == null) {
            return !isAssigned;
        }
        Optional<Seat> optionalSeat = seatRepository.findById(seatId);
        if (optionalSeat.isEmpty()) {
            return !isAssigned;
        }
        Seat asisSeat = optionalSeat.get();
        if (!Seat.isAvailableSeatStatus(asisSeat.seatStatus())) {
            return !isAssigned;
        }
        Seat tobeSeat = Seat.create(asisSeat.seatId(), asisSeat.performanceId(), asisSeat.concertId(), asisSeat.seatNo(), SeatStatus.WAITING_FOR_RESERVATION);
        try {
            seatRepository.save(tobeSeat);
            return isAssigned;
        } catch (Exception e) {
            return !isAssigned;
        }
    }

    /**
     * 좌석 임시 배정 상태 변경
     * @param reservationId 예약_ID
     * @param asisSeatStatus 기존 좌석_상태
     * @param tobeSeatStatus 이후 좌석_상태
     */
    public List<Seat> updateSeatStatusByReservationIdAndReservationStatus(Long reservationId, SeatStatus asisSeatStatus, SeatStatus tobeSeatStatus) {
        List<Seat> seats = seatRepository.findAllByReservationIdAndSeatStatus(reservationId, asisSeatStatus);
        List<Seat> savedSeats = new ArrayList<>();
        for (Seat asisSeat : seats) {
            Seat tobeSeat = Seat.create(asisSeat.seatId(), asisSeat.performanceId(), asisSeat.concertId(), asisSeat.seatNo(), tobeSeatStatus);
            savedSeats.add(seatRepository.save(tobeSeat));
        }
        return savedSeats;
    }
}
