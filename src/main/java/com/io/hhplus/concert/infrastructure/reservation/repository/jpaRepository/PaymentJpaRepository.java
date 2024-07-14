package com.io.hhplus.concert.infrastructure.reservation.repository.jpaRepository;

import com.io.hhplus.concert.infrastructure.reservation.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {
}
