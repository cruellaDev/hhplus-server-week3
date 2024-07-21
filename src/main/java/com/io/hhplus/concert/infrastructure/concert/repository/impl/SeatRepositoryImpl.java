package com.io.hhplus.concert.infrastructure.concert.repository.impl;

import com.io.hhplus.concert.common.enums.SeatStatus;
import com.io.hhplus.concert.domain.concert.entity.SeatEntity;
import com.io.hhplus.concert.domain.concert.service.model.SeatModel;
import com.io.hhplus.concert.domain.concert.repository.SeatRepository;
import com.io.hhplus.concert.infrastructure.concert.repository.jpaRepository.SeatJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepository {

    private final SeatJpaRepository seatJpaRepository;

    private boolean isEqualPerformanceId(Long performanceId, Long targetPerformanceId) {
        return performanceId != null && targetPerformanceId != null && performanceId.compareTo(targetPerformanceId) == 0;
    }

    private boolean isNotDeleted(Date deletedAt) {
        return deletedAt == null;
    }

    private boolean isAvailableStatus(SeatStatus seatStatus) {
        return seatStatus != null && seatStatus.equals(SeatStatus.AVAILABLE);
    }

    private SeatModel mapEntityToDto(SeatEntity entity) {
        return SeatModel.create(entity.getId(), entity.getPerformanceId(), entity.getConcertId(), entity.getSeatNo(), entity.getSeatStatus());
    }

    private SeatEntity mapDtoToEntity(SeatModel dto) {
        return SeatEntity.builder()
                .id(dto.seatId())
                .performanceId(dto.performanceId())
                .concertId(dto.concertId())
                .seatNo(dto.seatNo())
                .seatStatus(dto.seatStatus())
                .build();
    }

    @Override
    public List<SeatModel> findAvailableAllByPerformanceId(Long performanceId) {
        return seatJpaRepository.findAllByPerformanceId(performanceId)
                .stream()
                .filter(entity -> isNotDeleted(entity.getDeletedAt()) && isAvailableStatus(entity.getSeatStatus()))
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SeatModel> findAvailableOneByPerformanceIdAndSeatId(Long performanceId, Long seatId) {
        return seatJpaRepository.findById(seatId)
                .filter(entity -> isEqualPerformanceId(performanceId, entity.getPerformanceId())
                        && isNotDeleted(entity.getDeletedAt())
                        && isAvailableStatus(entity.getSeatStatus()))
                .map(this::mapEntityToDto);
    }

    @Override
    public Optional<SeatModel> findById(Long seatId) {
        return seatJpaRepository.findById(seatId)
                .map(this::mapEntityToDto);
    }

    @Override
    public SeatModel save(SeatModel seatModel) {
        return mapEntityToDto(seatJpaRepository.save(mapDtoToEntity(seatModel)));
    }

    @Override
    public List<SeatModel> findAllByReservationIdAndSeatStatus(Long reservationId, SeatStatus seatStatus) {
        return seatJpaRepository.findAllWaitingSeatsByReservationId(reservationId)
                .stream()
                .filter(entity -> entity.getSeatStatus().equals(seatStatus))
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }
}
