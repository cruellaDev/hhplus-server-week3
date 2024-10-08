package com.io.hhplus.concert.domain.concert;

import com.io.hhplus.concert.domain.concert.model.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ConcertRepository {
    Concert saveConcert(Concert concert);
    List<Concert> findConcerts();
    ConcertSchedule saveConcertSchedule(ConcertSchedule concertSchedule);
    List<ConcertSchedule> findConcertSchedules(Long concertId);
    ConcertSeat saveConcertSeat(ConcertSeat concertSeat);
    Optional<ConcertSeat> findConcertSeat(Long concertId, Long concertScheduleId);
    List<Ticket> findOccupiedSeatsFromTicket(Long concertId, Long concertScheduleId, String seatNumber);
    Reservation saveReservation(Reservation reservation);
    Ticket saveTicket(Ticket ticket);
    Optional<Concert> findConcert(Long concertId);
    Optional<ConcertSchedule> findConcertSchedule(Long concertId, Long concertScheduleId);
    Optional<Reservation> findReservation(Long reservationId, Long customerId);
    List<Ticket> findTickets(Long reservationId);
    Optional<Reservation> findCustomerReservationAlreadyExists(Long customerId, Long concertId, Long concertScheduleId, List<String> seatNumbers);
    List<Reservation> findCustomerReservationAlreadyExistsWithPessimisticLock(Long customerId, Long concertId, Long concertScheduleId, List<String> seatNumbers);
    List<Reservation> findReservationsAlreadyExists(Long concertId, Long concertScheduleId, List<String> seatNumbers);
    List<Reservation> findReservationsAlreadyExistsWithPessimisticLock(Long concertId, Long concertScheduleId, List<String> seatNumbers);
}
