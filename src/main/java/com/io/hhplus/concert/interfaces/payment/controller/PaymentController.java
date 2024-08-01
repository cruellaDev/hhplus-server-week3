package com.io.hhplus.concert.interfaces.payment.controller;

import com.io.hhplus.concert.application.payment.PaymentFacade;
import com.io.hhplus.concert.common.dto.CommonResponse;
import com.io.hhplus.concert.interfaces.payment.dto.PaymentDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("reservation")
public class PaymentController {

    private final PaymentFacade paymentFacade;

    /**
     * 결제 요청
     * @param requestBody 요청 정보
     * @return 응답 정보
     */
    @PostMapping("/pay")
    public CommonResponse<PaymentDto.CheckoutPaymentResponse> checkoutPayment(@RequestBody @Valid PaymentDto.CheckoutPaymentRequest requestBody) {
        return CommonResponse.success(PaymentDto.CheckoutPaymentResponse.from(paymentFacade.checkoutPayment(requestBody.toCommand())));
    }

}
