package com.io.hhplus.concert.infrastructure.reservation.repository.impl;

import com.io.hhplus.concert.common.enums.ReservationStatus;
import com.io.hhplus.concert.domain.reservation.entity.ReservationEntity;
import com.io.hhplus.concert.domain.reservation.service.model.ReservationModel;
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

    private ReservationModel mapEntityToDto(ReservationEntity entity) {
        return ReservationModel.create(
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

    private ReservationEntity mapDtoToEntity(ReservationModel dto) {
        return ReservationEntity.builder()
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
    public ReservationModel save(ReservationModel reservationModel) {
        return mapEntityToDto(reservationJpaRepository.save(mapDtoToEntity(reservationModel)));
    }

    @Override
    public Optional<ReservationModel> findByIdAndCustomerId(Long reservationId, Long customerId) {
        return reservationJpaRepository.findByIdAndCustomerId(reservationId, customerId)
                .filter(entity -> isNotDeleted(entity.getDeletedAt()))
                .map(this::mapEntityToDto);
    }

    @Override
    public List<ReservationModel> findAllByReservationStatus(ReservationStatus reservationStatus) {
        return reservationJpaRepository.findAllByReservationStatus(reservationStatus)
                .stream()
                .filter(entity -> isNotDeleted(entity.getDeletedAt()))
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }
}
