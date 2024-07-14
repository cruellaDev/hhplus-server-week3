package com.io.hhplus.concert.interfaces.customer.controller;

import com.io.hhplus.concert.application.customer.dto.CustomerPointHistoryResponse;
import com.io.hhplus.concert.application.customer.dto.CustomerPointRequest;
import com.io.hhplus.concert.application.customer.dto.CustomerResponse;
import com.io.hhplus.concert.application.customer.facade.CustomerFacade;
import com.io.hhplus.concert.interfaces.common.dto.CommonResponse;
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
    public CommonResponse<CustomerResponse> getCustomerPoint(@RequestHeader("customerId") Long customerId) {
        return CommonResponse.success(customerFacade.getCustomerPoint(customerId));
    }

    /**
     * 포인트 충전
     * @param requestBody 요청 정보
     * @return 응답 정보
     */
    @PostMapping("/point/charge")
    public CommonResponse<CustomerPointHistoryResponse> chargeCustomerPoint(@RequestBody CustomerPointRequest requestBody) {
        return CommonResponse.success(customerFacade.chargeCustomerPoint(requestBody));
    }
}
