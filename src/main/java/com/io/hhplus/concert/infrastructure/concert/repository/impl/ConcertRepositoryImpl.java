package com.io.hhplus.concert.infrastructure.concert.repository.impl;

import com.io.hhplus.concert.domain.concert.model.*;
import com.io.hhplus.concert.infrastructure.concert.entity.*;
import com.io.hhplus.concert.domain.concert.ConcertRepository;
import com.io.hhplus.concert.infrastructure.concert.repository.jpa.ConcertSeatJpaRepository;
import com.io.hhplus.concert.infrastructure.concert.repository.jpa.ConcertJpaRepository;
import com.io.hhplus.concert.infrastructure.concert.repository.jpa.ConcertScheduleJpaRepository;
import com.io.hhplus.concert.infrastructure.concert.repository.jpa.ReservationJpaRepository;
import com.io.hhplus.concert.infrastructure.concert.repository.jpa.TicketJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;
    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;
    private final ConcertSeatJpaRepository concertSeatJpaRepository;
    private final ReservationJpaRepository reservationJpaRepository;
    private final TicketJpaRepository ticketJpaRepository;

    @Override
    public List<Concert> findConcerts() {
        return concertJpaRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(ConcertEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConcertSchedule> findConcertSchedules(Long concertId) {
        return concertScheduleJpaRepository.findAllByDeletedAtIsNullAndConcertIdEquals(concertId)
                .stream()
                .map(ConcertScheduleEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ConcertSeat> findConcertSeat(Long concertId, Long concertScheduleId) {
        Optional<ConcertScheduleEntity> concertSchedule = concertScheduleJpaRepository.findByIdAndConcertIdAndDeletedAtIsNull(concertScheduleId, concertId);
        if (concertSchedule.isPresent()) {
            return concertSeatJpaRepository.findByConcertIdAndConcertScheduleIdAndDeletedAtIsNull(concertId, concertScheduleId)
                    .map(ConcertSeatEntity::toDomain);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Ticket> findNotOccupiedSeatFromTicket(Long concertId, Long concertScheduleId, String seatNumber) {
        return ticketJpaRepository.findNotOccupiedSeatFromTicket(concertId, concertScheduleId, seatNumber)
                .filter(TicketEntity::isReservable)
                .map(TicketEntity::toDomain);
    }

    @Override
    public Reservation saveReservation(Reservation reservation) {
        return reservationJpaRepository.save(ReservationEntity.from(reservation)).toDomain();
    }

    @Override
    public Ticket saveTicket(Ticket ticket) {
        return ticketJpaRepository.save(TicketEntity.from(ticket)).toDomain();
    }

    @Override
    public Optional<Concert> findConcert(Long concertId) {
        return concertJpaRepository.findById(concertId)
                .map(ConcertEntity::toDomain);
    }

    @Override
    public Optional<ConcertSchedule> findConcertSchedule(Long concertId, Long concertScheduleId) {
        return concertScheduleJpaRepository.findById(concertScheduleId)
                .filter(entity -> entity.isEqualConcertId(concertId))
                .map(ConcertScheduleEntity::toDomain);
    }

    @Override
    public Optional<Reservation> findReservation(Long reservationId, Long customerId) {
        return reservationJpaRepository.findById(reservationId)
                .filter(entity -> entity.isEqualCustomerId(customerId) && entity.isNotDeleted())
                .map(ReservationEntity::toDomain);
    }

    @Override
    public List<Ticket> findTickets(Long reservationId) {
        return ticketJpaRepository.findAllByReservationId(reservationId)
                .stream()
                .map(TicketEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Reservation> findReservationAlreadyExists(Long customerId, Long concertId, Long concertScheduleId, List<String> seatNumbers) {
        return reservationJpaRepository.findReservationAlreadyExists(customerId, concertId, concertScheduleId, seatNumbers);
    }
}
