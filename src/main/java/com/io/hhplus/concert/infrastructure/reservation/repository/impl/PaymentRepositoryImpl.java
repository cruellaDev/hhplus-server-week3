package com.io.hhplus.concert.infrastructure.reservation.repository.impl;

import com.io.hhplus.concert.domain.reservation.entity.PaymentEntity;
import com.io.hhplus.concert.domain.reservation.service.model.PaymentModel;
import com.io.hhplus.concert.domain.reservation.repository.PaymentRepository;
import com.io.hhplus.concert.infrastructure.reservation.repository.jpaRepository.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    private PaymentModel mapEntityToDto(PaymentEntity entity) {
        return PaymentModel.create(entity.getId(), entity.getReservationId(), entity.getPayMethod(), entity.getPayAmount(), entity.getRefundableAmount(), entity.getRefundAmount());
    }

    private PaymentEntity mapDtoToEntity(PaymentModel dto) {
        return PaymentEntity.builder()
                .id(dto.paymentId())
                .reservationId(dto.reservationId())
                .payMethod(dto.payMethod())
                .payAmount(dto.payAmount())
                .refundableAmount(dto.refundableAmount())
                .refundAmount(dto.refundAmount())
                .build();
    }

    @Override
    public PaymentModel save(PaymentModel paymentModel) {
        return mapEntityToDto(paymentJpaRepository.save(mapDtoToEntity(paymentModel)));
    }

}
