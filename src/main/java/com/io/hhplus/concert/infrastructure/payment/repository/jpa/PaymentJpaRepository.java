package com.io.hhplus.concert.infrastructure.payment.repository.jpa;

import com.io.hhplus.concert.infrastructure.payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {
}
