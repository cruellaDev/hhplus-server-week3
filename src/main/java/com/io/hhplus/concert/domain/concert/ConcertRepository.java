package com.io.hhplus.concert.domain.concert;

import com.io.hhplus.concert.domain.concert.model.*;

import java.util.List;
import java.util.Optional;

public interface ConcertRepository {
    List<Concert> findConcerts();
    List<ConcertSchedule> findConcertSchedules(Long concertId);
    Optional<ConcertSeat> findConcertSeat(Long concertId, Long concertScheduleId);
    Optional<Ticket> findNotOccupiedSeatFromTicket(Long concertId, Long concertScheduleId, String seatNumber);
    Reservation saveReservation(Reservation reservation);
    Ticket saveTicket(Ticket ticket);
    Optional<Concert> findConcert(Long concertId);
    Optional<ConcertSchedule> findConcertSchedule(Long concertId, Long concertScheduleId);
    Optional<Reservation> findReservation(Long reservationId, Long customerId);
    List<Ticket> findTickets(Long reservationId);
    Optional<Reservation> findReservationAlreadyExists(Long customerId, Long concertId, Long concertScheduleId, List<String> seatNumbers);
}
