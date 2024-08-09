package com.io.hhplus.concert.domain.payment;

import com.io.hhplus.concert.domain.payment.event.PaymentEvent;
import com.io.hhplus.concert.domain.payment.event.PaymentEventPublisher;
import com.io.hhplus.concert.domain.payment.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentEventPublisher paymentEventPublisher;
    private final PaymentRepository paymentRepository;

    /**
     * 결제 요청
     * @param command 결제 요청 command
     * @return 응답 정보
     */
    public Payment pay(PaymentCommand.PayCommand command) {
        Payment payment = paymentRepository.savePayment(Payment.create().pay(command));
        paymentEventPublisher.success(
                PaymentEvent.PaidSuccess.builder()
                        .token(command.getToken())
                        .customerId(command.getCustomerId())
                        .reservationId(command.getReservationId())
                        .payAmount(command.getPayAmount())
                        .build());

        return payment;
    }
}
