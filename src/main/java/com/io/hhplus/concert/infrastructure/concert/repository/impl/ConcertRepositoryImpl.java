package com.io.hhplus.concert.infrastructure.concert.repository.impl;

import com.io.hhplus.concert.common.enums.ConcertStatus;
import com.io.hhplus.concert.domain.concert.model.Concert;
import com.io.hhplus.concert.domain.concert.repository.ConcertRepository;
import com.io.hhplus.concert.infrastructure.concert.repository.jpaRepository.ConcertJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;

    private boolean isNotDeleted(Date deletedAt) {
        return deletedAt == null;
    }

    private boolean isAvailableStatus(ConcertStatus concertStatus) {
        return concertStatus != null && concertStatus.equals(ConcertStatus.AVAILABLE);
    }

    private Concert mapEntityToResponseModel(com.io.hhplus.concert.infrastructure.concert.entity.Concert entity) {
        return Concert.create(entity.getId(), entity.getConcertName(), entity.getArtistName(), entity.getConcertStatus());
    }

    @Override
    public Optional<Concert> findAvailableOneById(Long id) {
        return concertJpaRepository.findById(id)
                .filter(entity -> isNotDeleted(entity.getDeletedAt())
                        && isAvailableStatus(entity.getConcertStatus()))
                .map(this::mapEntityToResponseModel);
    }
}
