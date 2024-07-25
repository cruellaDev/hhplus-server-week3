package com.io.hhplus.concert.application.customer.facade;

import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.domain.customer.dto.ChargeCustomerPointServiceRequest;
import com.io.hhplus.concert.domain.customer.service.CustomerValidator;
import com.io.hhplus.concert.application.customer.dto.CustomerInfoWithCustomerPointHistory;
import com.io.hhplus.concert.application.customer.dto.CustomerInfo;
import com.io.hhplus.concert.common.enums.PointType;
import com.io.hhplus.concert.domain.customer.service.model.CustomerModel;
import com.io.hhplus.concert.domain.customer.service.model.CustomerPointHistoryModel;
import com.io.hhplus.concert.domain.customer.service.CustomerService;
import com.io.hhplus.concert.interfaces.customer.dto.ChargeCustomerPointDto;
import com.io.hhplus.concert.interfaces.customer.dto.CustomerPointBalanceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CustomerFacade {

    private final CustomerService customerService;
    private final CustomerValidator customerValidator;

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
