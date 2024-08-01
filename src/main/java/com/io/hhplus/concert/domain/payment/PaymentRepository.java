package com.io.hhplus.concert.domain.payment;

import com.io.hhplus.concert.domain.payment.model.Payment;

public interface PaymentRepository {
    Payment savePayment(Payment payment);
}
