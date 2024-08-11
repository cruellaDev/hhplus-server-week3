package com.io.hhplus.concert.interfaces.customer.controller;

import com.io.hhplus.concert.common.dto.CommonResponse;
import com.io.hhplus.concert.domain.customer.CustomerService;
import com.io.hhplus.concert.interfaces.customer.dto.CustomerDto;
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

    private final CustomerService customerService;

    /**
     * 고객 등록 요청
     * @param request 고객 등록 요청 정보
     * @return 고객 등록 응답 정보
     */
    @PostMapping("/register")
    public ResponseEntity<CommonResponse<CustomerDto.RegisterCustomerResponse>> register(@RequestBody CustomerDto.RegisterCustomerRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(CustomerDto.RegisterCustomerResponse.from(customerService.registerCustomer(request.toCommand()))));
    }


    /**
     * 고객 포인트 잔액 조회
     * @param customerId 고객_ID
     * @return 응답 정보
     */
    @GetMapping("/{customerId}/point")
    public ResponseEntity<CommonResponse<CustomerPointDto.CustomerPointBalanceResponse>> point(@PathVariable("customerId") Long customerId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(CustomerPointDto.CustomerPointBalanceResponse.from(customerService.getCustomerPointBalance(customerId))));
    }

    /**
     * 고객 포인트 충전
     * @param requestBody 요청 정보
     * @return 응답 정보
     */
    @PostMapping("/point/charge")
    public ResponseEntity<CommonResponse<CustomerPointDto.ChargeCustomerPointResponse>> chargePoint(@RequestBody @Valid CustomerPointDto.ChargeCustomerPointRequest requestBody) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body((CommonResponse.success(CustomerPointDto.ChargeCustomerPointResponse.from(customerService.chargeCustomerPoint(requestBody.toCommand())))));
    }
}
