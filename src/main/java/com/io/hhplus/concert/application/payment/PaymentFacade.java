package com.io.hhplus.concert.application.payment;

import com.io.hhplus.concert.application.payment.dto.CheckoutPaymentResultAndConfirmedReservationInfo;
import com.io.hhplus.concert.domain.concert.ConcertService;
import com.io.hhplus.concert.domain.concert.dto.ReservationInfo;
import com.io.hhplus.concert.domain.customer.CustomerService;
import com.io.hhplus.concert.domain.payment.PaymentService;
import com.io.hhplus.concert.domain.payment.model.Payment;
import com.io.hhplus.concert.domain.queue.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final TokenService tokenService;
    private final ConcertService concertService;
    private final PaymentService paymentService;
    private final CustomerService customerService;

    /**
     * 결제 요청
     * @param command 결제 요청 command
     * @return 응답 정보
     */
    @Transactional
    public CheckoutPaymentResultAndConfirmedReservationInfo checkoutPayment(UUID token, CompositeCommand.CheckoutPaymentCommand command) {
        tokenService.validateIndividualToken(token);
        ReservationInfo confirmedReservationInfo = concertService.confirmReservation(command.toConfirmReservationCommand());
        customerService.useCustomerPoint(command.toUseCustomerPointCommand());
        Payment checkoutPaymentResult = paymentService.completePayment(command.toCompletePaymentCommand());
        tokenService.expireIndividualToken(token);
        return CheckoutPaymentResultAndConfirmedReservationInfo.of(checkoutPaymentResult, confirmedReservationInfo);
    }
}
