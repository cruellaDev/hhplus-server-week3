package com.io.hhplus.concert.infrastructure.concert.repository.impl;

import com.io.hhplus.concert.domain.concert.model.*;
import com.io.hhplus.concert.infrastructure.concert.entity.*;
import com.io.hhplus.concert.domain.concert.repository.ConcertRepository;
import com.io.hhplus.concert.infrastructure.concert.repository.jpa.AreaJpaRepository;
import com.io.hhplus.concert.infrastructure.concert.repository.jpa.ConcertJpaRepository;
import com.io.hhplus.concert.infrastructure.concert.repository.jpa.PerformanceJpaRepository;
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
    private final PerformanceJpaRepository performanceJpaRepository;
    private final AreaJpaRepository areaJpaRepository;
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
    public List<Performance> findPerformances(Long concertId) {
        return performanceJpaRepository.findAllByDeletedAtIsNullAndConcertIdEquals(concertId)
                .stream()
                .map(PerformanceEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Area> findAreas(Long concertId, Long performanceId) {
        return areaJpaRepository.findAllByConcertIdAndPerformanceIdAndDeletedAtIsNull(concertId, performanceId)
                .stream()
                .filter(entity -> entity.isEqualConcertId(concertId)
                        && entity.isEqualPerformanceId(performanceId)
                        && entity.isNotDeleted())
                .map(AreaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Area> findArea(Long concertId, Long performanceId, Long areaId) {
        return areaJpaRepository.findById(areaId)
                .filter(entity -> entity.isEqualConcertId(concertId)
                        && entity.isEqualPerformanceId(performanceId)
                        && entity.isNotDeleted())
                .map(AreaEntity::toDomain);
    }

    @Override
    public Boolean existsAvailableSeat(Long concertId, Long performanceId, Long areaId, String seatNumber) {
        return ticketJpaRepository.findNotOccupiedSeatFromTicket(concertId, performanceId, areaId, seatNumber)
                .filter(TicketEntity::isReservable)
                .isPresent();
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
    public Optional<Performance> findPerformance(Long performanceId) {
        return performanceJpaRepository.findById(performanceId)
                .map(PerformanceEntity::toDomain);
    }

    @Override
    public Optional<Area> findArea(Long areaId) {
        return areaJpaRepository.findById(areaId)
                .map(AreaEntity::toDomain);
    }
}
