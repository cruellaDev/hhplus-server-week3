package com.io.hhplus.concert.domain.reservation.service;

import com.io.hhplus.concert.common.enums.PayMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PaymentValidator {

    public boolean isAvailablePayMethod(PayMethod payMethod) {
        return payMethod != null;
    }

    public boolean isNotAvailablePayMethod(PayMethod payMethod) {
        return !this.isAvailablePayMethod(payMethod);
    }

    public boolean isAvailablePayAmount(BigDecimal payAmount) {
        return payAmount != null && payAmount.compareTo(BigDecimal.ZERO) >= 0;
    }

    public boolean isNotAvailablePayAmount(BigDecimal payAmount) {
        return !this.isAvailablePayAmount(payAmount);
    }

    public boolean isEqualPayAmount(BigDecimal payAmount, BigDecimal targetAmount) {
        return payAmount != null && targetAmount != null && payAmount.compareTo(targetAmount) == 0;
    }

    public boolean isNotEqualPayAmount(BigDecimal payAmount, BigDecimal targetAmount) {
        return !this.isEqualPayAmount(payAmount, targetAmount);
    }

    /**
     * 결제금액과 티켓합계금액과 비교
     * @param sumOfPayAmount 결제 금액 합계
     * @param sumOrTicketPrice 티켓 금액 합계
     * @return 비굣값 동일 여부
     */
    public boolean meetsIfSumOfPayAmountEqualsToSumOfTicketsPrice(BigDecimal sumOfPayAmount, BigDecimal sumOrTicketPrice) {
        return this.isEqualPayAmount(sumOfPayAmount, sumOrTicketPrice);
    }

}
