package com.io.hhplus.concert.domain.payment;

import com.io.hhplus.concert.domain.payment.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    /**
     * 결제 완료
     * @param command 결제 완료 command
     * @return 응답 정보
     */
    public Payment completePayment(PaymentCommand.CompletePaymentCommand command) {
        return paymentRepository.savePayment(Payment.create().pay(command));
    }
}
