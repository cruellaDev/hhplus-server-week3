package com.io.hhplus.concert.domain.reservation.repository;

import com.io.hhplus.concert.domain.reservation.model.Payment;

public interface PaymentRepository {
    Payment save(Payment payment);
}
