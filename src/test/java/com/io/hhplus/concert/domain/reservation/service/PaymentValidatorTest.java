package com.io.hhplus.concert.domain.reservation.service;

import com.io.hhplus.concert.common.enums.PayMethod;
import com.io.hhplus.concert.domain.reservation.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PaymentValidatorTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentValidator paymentValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void isAvailablePayMethod_payMethod_is_null() {
        // given
        PayMethod payMethod = null;

        // when
        boolean isValid = paymentValidator.isAvailablePayMethod(payMethod);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailablePayMethod_has_payMethod() {
        // given
        PayMethod payMethod = PayMethod.POINT;

        // when
        boolean isValid = paymentValidator.isAvailablePayMethod(payMethod);

        // then
        assertTrue(isValid);
    }

    @Test
    void isAvailablePayAmount_is_null() {
        // given
        BigDecimal payAmount = null;

        // when
        boolean isValid = paymentValidator.isAvailablePayAmount(payAmount);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailablePayAmount_less_than_zero() {
        // given
        BigDecimal payAmount = BigDecimal.valueOf(10000).negate();

        // when
        boolean isValid = paymentValidator.isAvailablePayAmount(payAmount);

        // then
        assertFalse(isValid);
    }

    @Test
    void isAvailablePayAmount_over_zero() {
        // given
        BigDecimal payAmount = BigDecimal.valueOf(10000);

        // when
        boolean isValid = paymentValidator.isAvailablePayAmount(payAmount);

        // then
        assertTrue(isValid);
    }

    @Test
    void isEqualPayAmount_not_equal() {
        // given
        BigDecimal payAmount = BigDecimal.ZERO;
        BigDecimal targetAmount = BigDecimal.TEN;

        // when
        boolean isEqual = paymentValidator.isEqualPayAmount(payAmount, targetAmount);

        // then
        assertFalse(isEqual);
    }

    @Test
    void isEqualPayAmount_args_are_null() {
        // given
        BigDecimal payAmount = null;
        BigDecimal targetAmount = null;

        // when
        boolean isEqual = paymentValidator.isEqualPayAmount(payAmount, targetAmount);

        // then
        assertFalse(isEqual);
    }

    @Test
    void isEqualPayAmount_equal() {
        // given
        BigDecimal payAmount = BigDecimal.TEN;
        BigDecimal targetAmount = BigDecimal.TEN;

        // when
        boolean isEqual = paymentValidator.isEqualPayAmount(payAmount, targetAmount);

        // then
        assertTrue(isEqual);
    }

    @Test
    void meetsIfSumOfPayAmountEqualsToSumOfTicketsPrice_not_equal() {
        // given
        BigDecimal sumOfPayAmount = BigDecimal.ZERO;
        BigDecimal sumOrTicketPrice = BigDecimal.TEN;

        // when
        boolean isEqual = paymentValidator.meetsIfSumOfPayAmountEqualsToSumOfTicketsPrice(sumOfPayAmount, sumOrTicketPrice);

        // then
        assertFalse(isEqual);
    }

    @Test
    void meetsIfSumOfPayAmountEqualsToSumOfTicketsPrice_args_are_null() {
        // given
        BigDecimal sumOfPayAmount = null;
        BigDecimal sumOrTicketPrice = null;

        // when
        boolean isEqual = paymentValidator.meetsIfSumOfPayAmountEqualsToSumOfTicketsPrice(sumOfPayAmount, sumOrTicketPrice);

        // then
        assertFalse(isEqual);
    }

    @Test
    void meetsIfSumOfPayAmountEqualsToSumOfTicketsPrice_is_equal() {
        // given
        BigDecimal sumOfPayAmount = BigDecimal.TEN;
        BigDecimal sumOrTicketPrice = BigDecimal.TEN;

        // when
        boolean isEqual = paymentValidator.meetsIfSumOfPayAmountEqualsToSumOfTicketsPrice(sumOfPayAmount, sumOrTicketPrice);

        // then
        assertTrue(isEqual);
    }
}