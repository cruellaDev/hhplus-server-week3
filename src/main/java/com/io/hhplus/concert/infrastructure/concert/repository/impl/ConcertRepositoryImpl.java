package com.io.hhplus.concert.infrastructure.concert.repository.impl;

import com.io.hhplus.concert.infrastructure.concert.entity.ConcertEntity;
import com.io.hhplus.concert.domain.concert.model.Concert;
import com.io.hhplus.concert.domain.concert.repository.ConcertRepository;
import com.io.hhplus.concert.infrastructure.concert.repository.jpa.ConcertJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;

    @Override
    public Optional<Concert> findConcert(Long concertId) {
        return concertJpaRepository.findById(concertId)
                .map(ConcertEntity::toDomain);
    }

    @Override
    public List<Concert> findConcerts() {
        return concertJpaRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(ConcertEntity::toDomain)
                .collect(Collectors.toList());
    }



//    private boolean isNotDeleted(Date deletedAt) {
//        return deletedAt == null;
//    }
//
//    private boolean isAvailableStatus(ConcertStatus concertStatus) {
//        return concertStatus != null && concertStatus.equals(ConcertStatus.AVAILABLE);
//    }
//
//    private Concert mapEntityToDto(ConcertEntity entity) {
//        return Concert.create(entity.getId(), entity.getConcertName(), entity.getArtistName(), entity.getConcertStatus());
//    }
//
//    @Override
//    public List<Concert> findAvailableAll() {
//        return concertJpaRepository.findAll()
//                .stream()
//                .filter(entity -> isNotDeleted(entity.getDeletedAt()) && isAvailableStatus(entity.getConcertStatus()))
//                .map(this::mapEntityToDto)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public Optional<Concert> findAvailableOneById(Long id) {
//        return concertJpaRepository.findById(id)
//                .filter(entity -> isNotDeleted(entity.getDeletedAt())
//                        && isAvailableStatus(entity.getConcertStatus()))
//                .map(this::mapEntityToDto);
//    }
}
