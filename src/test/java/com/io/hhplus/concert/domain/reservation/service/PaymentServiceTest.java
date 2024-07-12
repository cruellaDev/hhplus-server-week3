package com.io.hhplus.concert.domain.reservation.service;

import com.io.hhplus.concert.common.enums.PayMethod;
import com.io.hhplus.concert.domain.reservation.model.Payment;
import com.io.hhplus.concert.domain.reservation.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 결제금액과 티켓합계금액과 비교 - 금액 같을 시
     */
    @Test
    void meetsIfSumOfPayAmountEqualsToSumOfTicketsPrice_when_equal() {
        // given
        BigDecimal sumOfPayAmount = BigDecimal.valueOf(10000);
        BigDecimal sumOfTicketPrice = BigDecimal.valueOf(10000);

        // when
        boolean isValid = paymentService.meetsIfSumOfPayAmountEqualsToSumOfTicketsPrice(sumOfPayAmount, sumOfTicketPrice);

        // then
        assertThat(isValid).isTrue();
    }

    /**
     * 결제금액과 티켓합계금액과 비교 - 금액 다를 시
     */
    @Test
    void meetsIfSumOfPayAmountEqualsToSumOfTicketsPrice_when_not_equal() {
        // given
        BigDecimal sumOfPayAmount = BigDecimal.valueOf(10000);
        BigDecimal sumOfTicketPrice = BigDecimal.valueOf(20000);

        // when
        boolean isValid = paymentService.meetsIfSumOfPayAmountEqualsToSumOfTicketsPrice(sumOfPayAmount, sumOfTicketPrice);

        // then
        assertThat(isValid).isFalse();
    }

    /**
     * 결제 저장
     */
    @Test
    void savePayment() {
        // given
        Payment payment = Payment.create(
                1L,
                1L,
                PayMethod.POINT,
                BigDecimal.valueOf(10000),
                BigDecimal.valueOf(10000),
                BigDecimal.ZERO
        );
        given(paymentRepository.save(any(Payment.class))).willReturn(payment);

        // when
        Payment result = paymentService.savePayment(payment);

        // then
        assertAll(() -> assertEquals(payment.paymentId(), result.paymentId()),
                () -> assertEquals(payment.reservationId(), result.reservationId()),
                () -> assertEquals(payment.payMethod(), result.payMethod()),
                () -> assertEquals(payment.payAmount(), result.payAmount()),
                () -> assertEquals(payment.refundableAmount(), result.refundableAmount()),
                () -> assertEquals(payment.refundAmount(), result.refundAmount()));
    }

}