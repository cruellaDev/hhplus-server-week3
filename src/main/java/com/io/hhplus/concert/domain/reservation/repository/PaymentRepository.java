package com.io.hhplus.concert.domain.reservation.repository;

import com.io.hhplus.concert.domain.reservation.service.model.PaymentModel;

public interface PaymentRepository {
    PaymentModel save(PaymentModel paymentModel);
}
