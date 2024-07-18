package com.io.hhplus.concert.domain.concert.repository;

import com.io.hhplus.concert.domain.concert.service.model.ConcertModel;

import java.util.List;
import java.util.Optional;

public interface ConcertRepository {
    List<ConcertModel> findAvailableAll();
    Optional<ConcertModel> findAvailableOneById(Long id);
}
