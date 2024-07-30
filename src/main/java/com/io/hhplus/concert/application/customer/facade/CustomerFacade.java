package com.io.hhplus.concert.application.customer.facade;

import com.io.hhplus.concert.application.customer.dto.ChargeCustomerPointServiceRequest;
import com.io.hhplus.concert.domain.customer.service.CustomerService;
import com.io.hhplus.concert.interfaces.customer.dto.ChargeCustomerPointDto;
import com.io.hhplus.concert.interfaces.customer.dto.CustomerPointBalanceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerFacade {

    private final CustomerService customerService;

    /**
     * 고객 포인트 잔액 조회
     * @param customerId 고객_ID
     * @return 응답 정보
     */
    public CustomerPointBalanceDto.Response getCustomerPointBalance(Long customerId) {
        return CustomerPointBalanceDto.Response.from(customerService.getCustomerPointBalance(customerId));
    }

    /**
     * 고객 포인트 충전
     * @param serviceRequest 서비스 요청 정보
     * @return 고객 포인트 충전 내역
     */
    public ChargeCustomerPointDto.Response chargeCustomerPoint(ChargeCustomerPointServiceRequest serviceRequest) {
        return ChargeCustomerPointDto.Response.from(customerService.chargeCustomerPoint(serviceRequest.getCustomerId(), serviceRequest.getAmount()));
    }
}
