package com.io.hhplus.concert.domain.payment.service;

import com.io.hhplus.concert.domain.payment.PaymentService;
import com.io.hhplus.concert.domain.payment.PaymentRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Disabled
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    /**
//     * 결제 저장
//     */
//    @Test
//    void savePayment() {
//        // given
//        PaymentModel paymentModel = PaymentModel.create(
//                1L,
//                1L,
//                PayMethod.POINT,
//                BigDecimal.valueOf(10000),
//                BigDecimal.valueOf(10000),
//                BigDecimal.ZERO
//        );
//        given(paymentRepository.save(any(PaymentModel.class))).willReturn(paymentModel);
//
//        // when
//        PaymentModel result = paymentService.savePayment(paymentModel);
//
//        // then
//        assertAll(() -> assertEquals(paymentModel.paymentId(), result.paymentId()),
//                () -> assertEquals(paymentModel.reservationId(), result.reservationId()),
//                () -> assertEquals(paymentModel.payMethod(), result.payMethod()),
//                () -> assertEquals(paymentModel.payAmount(), result.payAmount()),
//                () -> assertEquals(paymentModel.refundableAmount(), result.refundableAmount()),
//                () -> assertEquals(paymentModel.refundAmount(), result.refundAmount()));
//    }

}