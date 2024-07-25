package com.io.hhplus.concert.interfaces.customer.controller;

import com.io.hhplus.concert.interfaces.customer.dto.ChargeCustomerPointDto;
import com.io.hhplus.concert.interfaces.customer.dto.CustomerPointBalanceDto;
import com.io.hhplus.concert.application.customer.facade.CustomerFacade;
import com.io.hhplus.concert.common.dto.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("customers")
public class CustomerController {

    private final CustomerFacade customerFacade;

    /**
     * 고객 포인트 잔액 조회
     * @param customerId 고객_ID
     * @return 응답 정보
     */
    @GetMapping("/{customerId}/point")
    public CommonResponse<CustomerPointBalanceDto.Response> getCustomerPoint(@PathVariable("customerId") Long customerId) {
        return CommonResponse.success(customerFacade.getCustomerPointBalance(customerId));
    }

    /**
     * 고객 포인트 충전
     * @param requestBody 요청 정보
     * @return 응답 정보
     */
    @PostMapping("/point/charge")
    public CommonResponse<ChargeCustomerPointDto.Response> chargeCustomerPoint(@RequestBody @Valid ChargeCustomerPointDto.Request requestBody) {
        return CommonResponse.success(customerFacade.chargeCustomerPoint(requestBody.toServiceRequest()));
    }
}
