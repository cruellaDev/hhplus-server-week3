package com.io.hhplus.concert.domain.concert.repository;

import com.io.hhplus.concert.domain.concert.model.Concert;

import java.util.List;
import java.util.Optional;

public interface ConcertRepository {
    Optional<Concert> findConcert(Long concertId);
    List<Concert> findConcerts();
}
