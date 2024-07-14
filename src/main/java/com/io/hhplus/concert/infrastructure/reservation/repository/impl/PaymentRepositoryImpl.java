package com.io.hhplus.concert.infrastructure.reservation.repository.impl;

import com.io.hhplus.concert.domain.reservation.model.Payment;
import com.io.hhplus.concert.domain.reservation.repository.PaymentRepository;
import com.io.hhplus.concert.infrastructure.reservation.repository.jpaRepository.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    private Payment mapEntityToDto(com.io.hhplus.concert.infrastructure.reservation.entity.Payment entity) {
        return Payment.create(entity.getId(), entity.getReservationId(), entity.getPayMethod(), entity.getPayAmount(), entity.getRefundableAmount(), entity.getRefundAmount());
    }

    private com.io.hhplus.concert.infrastructure.reservation.entity.Payment mapDtoToEntity(Payment dto) {
        return com.io.hhplus.concert.infrastructure.reservation.entity.Payment.builder()
                .id(dto.paymentId())
                .reservationId(dto.reservationId())
                .payMethod(dto.payMethod())
                .payAmount(dto.payAmount())
                .refundableAmount(dto.refundableAmount())
                .refundAmount(dto.refundAmount())
                .build();
    }

    @Override
    public Payment save(Payment payment) {
        return mapEntityToDto(paymentJpaRepository.save(mapDtoToEntity(payment)));
    }

}
