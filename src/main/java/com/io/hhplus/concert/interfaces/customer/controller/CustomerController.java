package com.io.hhplus.concert.interfaces.customer.controller;

import com.io.hhplus.concert.application.customer.CustomerFacade;
import com.io.hhplus.concert.common.dto.CommonResponse;
import com.io.hhplus.concert.interfaces.customer.dto.CustomerPointDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CommonResponse<CustomerPointDto.CustomerPointBalanceResponse>> getCustomerPoint(@PathVariable("customerId") Long customerId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(customerFacade.getCustomerPointBalance(customerId)));
    }

    /**
     * 고객 포인트 충전
     * @param requestBody 요청 정보
     * @return 응답 정보
     */
    @PostMapping("/point/charge")
    public ResponseEntity<CommonResponse<CustomerPointDto.ChargeCustomerPointResponse>> chargeCustomerPoint(@RequestBody @Valid CustomerPointDto.ChargeCustomerPointRequest requestBody) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body((CommonResponse.success(customerFacade.chargeCustomerPoint(requestBody.toCommand()))));
    }
}
