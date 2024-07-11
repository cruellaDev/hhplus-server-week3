package com.io.hhplus.concert.domain.reservation.service;

import com.io.hhplus.concert.common.enums.TicketStatus;
import com.io.hhplus.concert.domain.reservation.model.Payment;
import com.io.hhplus.concert.domain.reservation.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    /**
     * 결제금액과 티켓합계금액과 비교
     * @param sumOfPayAmount 결제 금액 합계
     * @param sumOrTicketPrice 티켓 금액 합계
     * @return 비굣값 동일 여부
     */
    public boolean meetsIfSumOfPayAmountEqualsToSumOfTicketsPrice(BigDecimal sumOfPayAmount, BigDecimal sumOrTicketPrice) {
        return Payment.isEqualPayAmount(sumOfPayAmount, sumOrTicketPrice);
    }

    /**
     * 결제 정보 저장
     * @param payment 결제 정보
     * @return 결제 저장 정보
     */
    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }
}
