package com.io.hhplus.concert.presentation.customer.controller;

import com.io.hhplus.concert.presentation.common.dto.ErrorResponseBody;
import com.io.hhplus.concert.presentation.customer.dto.request.PostBalanceRequestBody;
import com.io.hhplus.concert.presentation.customer.dto.request.PostChargeRequestBody;
import com.io.hhplus.concert.presentation.customer.dto.response.PostCustomerPointResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RequestMapping("customer")
@RestController
public class CustomerController {

    /**
     * 포인트 잔액 조회
     * @param requestBody 요청 정보
     * @return 응답 정보
     */
    @PostMapping("/point")
    public ResponseEntity<PostCustomerPointResponseBody> balanceCustomerPoint(@RequestBody PostBalanceRequestBody requestBody) {
        return ResponseEntity.ok().body(new PostCustomerPointResponseBody(requestBody.customerId(), BigDecimal.TEN));
    }

    /**
     * 포인트 충전
     * @param requestBody 요청 정보
     * @return 응답 정보
     */
    @PostMapping("/point/charge")
    public ResponseEntity<Object> chargeCustomerPoint(@RequestBody PostChargeRequestBody requestBody) {
        if (requestBody.amount() == null || requestBody.amount().compareTo(BigDecimal.TEN) < 0) {
            return ResponseEntity.status(500).body(new ErrorResponseBody("ILLEGAL_ARGUMENTS", "충전 금액은 10원 이상이어야 합니다."));
        }
        return ResponseEntity.ok().body(new PostCustomerPointResponseBody(requestBody.customerId(), requestBody.amount()));
    }
}
