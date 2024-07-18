package com.io.hhplus.concert.infrastructure.concert.repository.impl;

import com.io.hhplus.concert.common.enums.ConcertStatus;
import com.io.hhplus.concert.domain.concert.entity.ConcertEntity;
import com.io.hhplus.concert.domain.concert.service.model.ConcertModel;
import com.io.hhplus.concert.domain.concert.repository.ConcertRepository;
import com.io.hhplus.concert.infrastructure.concert.repository.jpaRepository.ConcertJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private ConcertModel mapEntityToDto(ConcertEntity entity) {
        return ConcertModel.create(entity.getId(), entity.getConcertName(), entity.getArtistName(), entity.getConcertStatus());
    }

    @Override
    public List<ConcertModel> findAvailableAll() {
        return concertJpaRepository.findAll()
                .stream()
                .filter(entity -> isNotDeleted(entity.getDeletedAt()) && isAvailableStatus(entity.getConcertStatus()))
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ConcertModel> findAvailableOneById(Long id) {
        return concertJpaRepository.findById(id)
                .filter(entity -> isNotDeleted(entity.getDeletedAt())
                        && isAvailableStatus(entity.getConcertStatus()))
                .map(this::mapEntityToDto);
    }
}
