package com.io.hhplus.concert.domain.concert.model;

import com.io.hhplus.concert.common.enums.SeatStatus;

public record Seat(
        Long seatId,
        Long performanceId,
        Long concertId,
        String seatNo,
        SeatStatus seatStatus
) {
    public static Seat create(Long seatId, Long performanceId, Long concertId, String seatNo, SeatStatus seatStatus) {
        return new Seat(seatId, performanceId, concertId, seatNo, seatStatus);
    }

    public static Seat noContents() {
        return create(-1L, -1L, -1L, "N/A", SeatStatus.NOT_AVAILABLE);
    }

    public static boolean isAvailableSeatId(Long seatId) {
        return seatId != null && seatId.compareTo(0L) > 0;
    }

    public static boolean isAvailablePerformanceId(Long performanceId) {
        return performanceId != null && performanceId.compareTo(0L) > 0;
    }

    public static boolean isAvailableConcertId(Long concertId) {
        return concertId != null && concertId.compareTo(0L) > 0;
    }

    public static boolean isAvailableSeatNo(String seatNo) {
        return seatNo != null && !seatNo.isBlank();
    }

    public static boolean isAvailableSeatStatus(SeatStatus seatStatus) {
        return seatStatus != null && seatStatus.equals(SeatStatus.AVAILABLE);
    }

    public static boolean isOccupied(SeatStatus seatStatus) {
        return seatStatus != null && seatStatus.equals(SeatStatus.WAITING_FOR_RESERVATION);
    }

    public static boolean isTaken(SeatStatus seatStatus) {
        return seatStatus != null && seatStatus.equals(SeatStatus.TAKEN);
    }
}
