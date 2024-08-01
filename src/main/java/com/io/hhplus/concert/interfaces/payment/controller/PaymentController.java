package com.io.hhplus.concert.interfaces.payment.controller;

import com.io.hhplus.concert.application.payment.PaymentFacade;
import com.io.hhplus.concert.common.GlobalConstants;
import com.io.hhplus.concert.common.dto.CommonResponse;
import com.io.hhplus.concert.interfaces.payment.dto.PaymentDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


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
    public ResponseEntity<CommonResponse<PaymentDto.CheckoutPaymentResponse>> checkoutPayment(@RequestHeader(value = GlobalConstants.HEADER_AUTHORIZATION) String authorizationHeader,
                                                                                              @RequestBody @Valid PaymentDto.CheckoutPaymentRequest requestBody) {
        UUID token = UUID.fromString(authorizationHeader.replace(GlobalConstants.PREFIX_BEARER, ""));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(PaymentDto.CheckoutPaymentResponse.from(paymentFacade.checkoutPayment(token, requestBody.toCommand()))));
    }

}
