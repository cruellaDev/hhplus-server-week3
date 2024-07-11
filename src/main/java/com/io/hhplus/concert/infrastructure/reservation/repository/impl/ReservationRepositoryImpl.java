package com.io.hhplus.concert.infrastructure.reservation.repository.impl;

import com.io.hhplus.concert.common.enums.ReservationStatus;
import com.io.hhplus.concert.domain.reservation.model.Reservation;
import com.io.hhplus.concert.domain.reservation.repository.ReservationRepository;
import com.io.hhplus.concert.infrastructure.reservation.repository.jpaRepository.ReservationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {

    private final ReservationJpaRepository reservationJpaRepository;

    private boolean isNotDeleted(Date deletedAt) {
        return deletedAt == null;
    }

    private Reservation mapEntityToDto(com.io.hhplus.concert.infrastructure.reservation.entity.Reservation entity) {
        return Reservation.create(
                entity.getId(),
                entity.getCustomerId(),
                entity.getReserverName(),
                entity.getReservationStatus(),
                entity.getReservationStatusChangedAt(),
                entity.getReceiveMethod(),
                entity.getReceiverName(),
                entity.getReceivePostcode(),
                entity.getReceiveBaseAddress(),
                entity.getReceiveDetailAddress());
    }

    private com.io.hhplus.concert.infrastructure.reservation.entity.Reservation mapDtoToEntity(Reservation dto) {
        return com.io.hhplus.concert.infrastructure.reservation.entity.Reservation.builder()
                .id(dto.reservationId())
                .customerId(dto.customerId())
                .reserverName(dto.reserverName())
                .reservationStatus(dto.reservationStatus())
                .reservationStatusChangedAt(dto.reservationStatusChangedAt())
                .receiveMethod(dto.receiveMethod())
                .receiverName(dto.receiverName())
                .receivePostcode(dto.receiverPostcode())
                .receiveBaseAddress(dto.receiverBaseAddress())
                .receiveDetailAddress(dto.receiverDetailAddress())
                .build();
    }

    @Override
    public Reservation save(Reservation reservation) {
        return mapEntityToDto(reservationJpaRepository.save(mapDtoToEntity(reservation)));
    }

    @Override
    public Optional<Reservation> findByIdAndCustomerId(Long reservationId, Long customerId) {
        return reservationJpaRepository.findByIdAndCustomerId(reservationId, customerId)
                .filter(entity -> isNotDeleted(entity.getDeletedAt()))
                .map(this::mapEntityToDto);
    }

    @Override
    public List<Reservation> findAllByReservationStatus(ReservationStatus reservationStatus) {
        return reservationJpaRepository.findAllByReservationStatus(reservationStatus)
                .stream()
                .filter(entity -> isNotDeleted(entity.getDeletedAt()))
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }
}
