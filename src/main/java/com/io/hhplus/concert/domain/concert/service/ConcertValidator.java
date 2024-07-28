package com.io.hhplus.concert.domain.concert.service;

import com.io.hhplus.concert.common.enums.ConcertStatus;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.enums.SeatStatus;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.domain.concert.model.Concert;
import com.io.hhplus.concert.domain.concert.model.Performance;
import com.io.hhplus.concert.domain.concert.model.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class ConcertValidator {

    // insert 시 id는 null 이어야 하기 때문에 select, update 시에만 사용.
    public boolean isAvailableConcertId(Long concertId) {
        return concertId != null && concertId.compareTo(0L) > 0;
    }

    public boolean isNotAvailableConcertId(Long concertId) {
        return !this.isAvailableConcertId(concertId);
    }

    public boolean isAvailableConcertName(String concertName) {
        return concertName != null && !concertName.isBlank() && !concertName.equals("N/A");
    }

    public boolean isNotAvailableConcertName(String concertName) {
        return !this.isAvailableConcertName(concertName);
    }

    public boolean isAvailableStatus(ConcertStatus concertStatus) {
        return concertStatus != null && concertStatus.equals(ConcertStatus.AVAILABLE);
    }

    public boolean isNotAvailableStatus(ConcertStatus concertStatus) {
        return !this.isAvailableStatus(concertStatus);
    }

    public boolean isAvailablePerformanceId(Long performanceId) {
        return performanceId != null && performanceId.compareTo(0L) > 0;
    }

    public boolean isNotAvailablePerformanceId(Long performanceId) {
        return !this.isAvailablePerformanceId(performanceId);
    }

    public boolean isAvailableCapacityLimit(Integer capacityLimit) {
        return capacityLimit != null && capacityLimit > 0;
    }

    public boolean isNotAvailableCapacityLimit(Integer capacityLimit) {
        return !this.isAvailableCapacityLimit(capacityLimit);
    }

    public boolean isPerformAtAfterTargetDate(Date performedAt, Date targetDate) {
        return performedAt != null && targetDate != null && performedAt.after(targetDate);
    }

    public boolean isAvailableSeatId(Long seatId) {
        return seatId != null && seatId.compareTo(0L) > 0;
    }

    public boolean isNotAvailableSeatId(Long seatId) {
        return !this.isAvailableSeatId(seatId);
    }

    public boolean isAvailableSeatNo(String seatNo) {
        return seatNo != null && !seatNo.isBlank();
    }

    public boolean isNotAvailableSeatNo(String seatNo) {
        return !this.isAvailableSeatNo(seatNo);
    }

    public boolean isAvailableSeatStatus(SeatStatus seatStatus) {
        return seatStatus != null && seatStatus.equals(SeatStatus.AVAILABLE);
    }

    public boolean isNotAvailableSeatStatus(SeatStatus seatStatus) {
        return !this.isAvailableSeatStatus(seatStatus);
    }

    public boolean isOccupied(SeatStatus seatStatus) {
        return seatStatus != null && seatStatus.equals(SeatStatus.WAITING_FOR_RESERVATION);
    }

    public boolean isNotOccupied(SeatStatus seatStatus) {
        return !this.isOccupied(seatStatus);
    }

    public boolean isTaken(SeatStatus seatStatus) {
        return seatStatus != null && seatStatus.equals(SeatStatus.TAKEN);
    }

    public boolean isNotTaken(SeatStatus seatStatus) {
        return !this.isTaken(seatStatus);
    }

    /**
     * 예약 가능 콘서트 검증
     * @param concert 콘서트 정보
     */
    public void checkIfConcertToBeReserved(Concert concert) {
        if (concert != null
                && isAvailableConcertId(concert.concertId())
                && isAvailableStatus(concert.concertStatus())) {
            return;
        }
        throw new CustomException(ResponseMessage.NOT_AVAILABLE, "해당 콘서트는 예약이 불가능합니다.");
    }

    /**
     * 예약 가능 공연 검증
     * @param performance 공연 정보
     * @param currentDate 현재일시
     */
    public void checkIfPerformanceToBeReserved(Performance performance, Date currentDate) {
        if (performance != null
                && this.isAvailablePerformanceId(performance.performanceId())
                && this.isAvailableCapacityLimit(performance.capacityLimit())
                && this.isPerformAtAfterTargetDate(performance.performedAt(), currentDate)) {
            return;
        }
        throw new CustomException(ResponseMessage.NOT_AVAILABLE, "해당 공연은 예약이 불가능합니다.");
    }

    /**
     * 예약 가능 좌석 검증
     * @param seat 좌석 정보
     */
    public void checkIfSeatToBeReserved(Seat seat) {
        if (seat != null
                && this.isAvailablePerformanceId(seat.performanceId())
                && this.isAvailableConcertId(seat.concertId())
                && this.isAvailableSeatNo(seat.seatNumber())
                && this.isAvailableSeatStatus(seat.seatStatus())) {
            return;
        }
        throw new CustomException(ResponseMessage.NOT_AVAILABLE, "해당 좌석은 예약이 불가능합니다.");
    }

//    /**
//     * 좌석 배정 가능 검증
//     * @param seatId 좌석_ID
//     */
//    public void checkIfSeatNotAssigned(Long seatId) {
//        Optional<Seat> optionalSeat = seatRepository.findById(seatId);
//        if (optionalSeat.isEmpty()) {
//            throw new CustomException(ResponseMessage.NOT_FOUND, "좌석이 없습니다.");
//        }
//        Seat seat = optionalSeat.get();
//        if (this.isOccupied(seat.seatStatus())) {
//            throw new CustomException(ResponseMessage.NOT_AVAILABLE, "이미 선점된 좌석입니다.");
//        }
//        if (this.isTaken(seat.seatStatus())) {
//            throw new CustomException(ResponseMessage.NOT_AVAILABLE, "이미 예약된 좌석입니다.");
//        }
//        if (this.isNotAvailableSeatStatus(seat.seatStatus())) {
//            throw new CustomException(ResponseMessage.NOT_AVAILABLE, "예약 가능한 상태가 아닙니다.");
//        }
//    }
}
