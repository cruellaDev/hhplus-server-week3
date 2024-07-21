package com.io.hhplus.concert.domain.reservation.service;

import com.io.hhplus.concert.domain.reservation.service.model.PaymentModel;
import com.io.hhplus.concert.domain.reservation.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    /**
     * 결제 정보 저장
     * @param paymentModel 결제 정보
     * @return 결제 저장 정보
     */
    public PaymentModel savePayment(PaymentModel paymentModel) {
        return paymentRepository.save(paymentModel);
    }
}
