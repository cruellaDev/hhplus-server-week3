package com.io.hhplus.concert.domain.concert.repository;

import com.io.hhplus.concert.domain.concert.model.*;

import java.util.List;
import java.util.Optional;

public interface ConcertRepository {
    List<Concert> findConcerts();
    List<Performance> findPerformances(Long concertId);
    List<Area> findAreas(Long concertId, Long performanceId);
    Optional<Area> findArea(Long concertId, Long performanceId, Long areaId);
    Boolean existsAvailableSeat(Long concertId, Long performanceId, Long areaId, String seatNumber);
    Reservation saveReservation(Reservation reservation);
    Ticket saveTicket(Ticket ticket);
    Optional<Concert> findConcert(Long concertId);
    Optional<Performance> findPerformance(Long performanceId);
    Optional<Area> findArea(Long areaId);
}
