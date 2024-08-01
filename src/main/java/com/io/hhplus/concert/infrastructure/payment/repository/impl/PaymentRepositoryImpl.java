package com.io.hhplus.concert.infrastructure.payment.repository.impl;

import com.io.hhplus.concert.domain.payment.model.Payment;
import com.io.hhplus.concert.domain.payment.PaymentRepository;
import com.io.hhplus.concert.infrastructure.payment.entity.PaymentEntity;
import com.io.hhplus.concert.infrastructure.payment.repository.jpa.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment savePayment(Payment payment) {
        return paymentJpaRepository.save(PaymentEntity.from(payment)).toDomain();
    }

}
