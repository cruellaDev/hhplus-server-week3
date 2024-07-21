package com.io.hhplus.concert.interfaces.customer.controller;

import com.io.hhplus.concert.application.customer.dto.CustomerInfoWithCustomerPointHistory;
import com.io.hhplus.concert.interfaces.customer.dto.request.CustomerPointRequest;
import com.io.hhplus.concert.application.customer.dto.CustomerInfo;
import com.io.hhplus.concert.application.customer.facade.CustomerFacade;
import com.io.hhplus.concert.common.dto.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("customer")
public class CustomerController {

    private final CustomerFacade customerFacade;

    /**
     * 포인트 잔액 조회
     * @param customerId 고객_ID
     * @return 응답 정보
     */
    @GetMapping("/point")
    public CommonResponse<CustomerInfo> getCustomerPoint(@RequestHeader("customerId") Long customerId) {
        return CommonResponse.success(customerFacade.getCustomerPoint(customerId));
    }

    /**
     * 포인트 충전
     * @param requestBody 요청 정보
     * @return 응답 정보
     */
    @PostMapping("/point/charge")
    public CommonResponse<CustomerInfoWithCustomerPointHistory> chargeCustomerPoint(@RequestBody @Valid CustomerPointRequest requestBody) {
        return CommonResponse.success(customerFacade.chargeCustomerPoint(requestBody.getCustomerId(), requestBody.getPointAmount()));
    }
}
