package com.io.hhplus.concert.infrastructure.concert.repository.impl;

import com.io.hhplus.concert.infrastructure.concert.entity.SeatEntity;
import com.io.hhplus.concert.domain.concert.model.Seat;
import com.io.hhplus.concert.domain.concert.repository.SeatRepository;
import com.io.hhplus.concert.infrastructure.concert.repository.jpa.SeatJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepository {

    private final SeatJpaRepository seatJpaRepository;

    @Override
    public List<Seat> findSeats(Long concertId, Long performanceId) {
        return seatJpaRepository.findAllByConcertIdAndPerformanceId(concertId, performanceId)
                .stream()
                .map(SeatEntity::toDomain)
                .collect(Collectors.toList());
    }

//    private boolean isEqualPerformanceId(Long performanceId, Long targetPerformanceId) {
//        return performanceId != null && targetPerformanceId != null && performanceId.compareTo(targetPerformanceId) == 0;
//    }
//
//    private boolean isNotDeleted(Date deletedAt) {
//        return deletedAt == null;
//    }
//
//    private boolean isAvailableStatus(SeatStatus seatStatus) {
//        return seatStatus != null && seatStatus.equals(SeatStatus.AVAILABLE);
//    }
//
//    private Seat mapEntityToDto(SeatEntity entity) {
//        return Seat.create(entity.getId(), entity.getPerformanceId(), entity.getConcertId(), entity.getSeatNo(), entity.getSeatStatus());
//    }
//
//    private SeatEntity mapDtoToEntity(Seat dto) {
//        return SeatEntity.builder()
//                .id(dto.seatId())
//                .performanceId(dto.performanceId())
//                .concertId(dto.concertId())
//                .seatNo(dto.seatNo())
//                .seatStatus(dto.seatStatus())
//                .build();
//    }
//
//    @Override
//    public List<Seat> findAvailableAllByPerformanceId(Long performanceId) {
//        return seatJpaRepository.findAllByPerformanceId(performanceId)
//                .stream()
//                .filter(entity -> isNotDeleted(entity.getDeletedAt()) && isAvailableStatus(entity.getSeatStatus()))
//                .map(this::mapEntityToDto)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public Optional<Seat> findAvailableOneByPerformanceIdAndSeatId(Long performanceId, Long seatId) {
//        return seatJpaRepository.findById(seatId)
//                .filter(entity -> isEqualPerformanceId(performanceId, entity.getPerformanceId())
//                        && isNotDeleted(entity.getDeletedAt())
//                        && isAvailableStatus(entity.getSeatStatus()))
//                .map(this::mapEntityToDto);
//    }
//
//    @Override
//    public Optional<Seat> findById(Long seatId) {
//        return seatJpaRepository.findById(seatId)
//                .map(this::mapEntityToDto);
//    }
//
//    @Override
//    public Seat save(Seat seat) {
//        return mapEntityToDto(seatJpaRepository.save(mapDtoToEntity(seat)));
//    }
//
//    @Override
//    public List<Seat> findAllByReservationIdAndSeatStatus(Long reservationId, SeatStatus seatStatus) {
//        return seatJpaRepository.findAllWaitingSeatsByReservationId(reservationId)
//                .stream()
//                .filter(entity -> entity.getSeatStatus().equals(seatStatus))
//                .map(this::mapEntityToDto)
//                .collect(Collectors.toList());
//    }
}
