package com.io.hhplus.concert.application.customer.facade;

import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.domain.customer.service.CustomerValidator;
import com.io.hhplus.concert.application.customer.dto.CustomerInfoWithCustomerPointHistory;
import com.io.hhplus.concert.application.customer.dto.CustomerInfo;
import com.io.hhplus.concert.common.enums.PointType;
import com.io.hhplus.concert.domain.customer.service.model.CustomerModel;
import com.io.hhplus.concert.domain.customer.service.model.CustomerPointHistoryModel;
import com.io.hhplus.concert.domain.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CustomerFacade {

    private final CustomerService customerService;
    private final CustomerValidator customerValidator;

    /**
     * 고객 포인트 조회
     * @param customerId 고객_ID
     * @return 응답 정보
     */
    public CustomerInfo getCustomerPoint(Long customerId) {
        if (customerId == null) throw new CustomException(ResponseMessage.INVALID, "고객_ID가 존재하지 않습니다.");

        // 고객 확인
        CustomerModel customerModel;
        // 고객 조회
        customerModel = customerService.getAvailableCustomer(customerId);
        // 고객 검증
        customerValidator.checkIfCustomerValid(customerModel);

        // 고객 포인트 잔액 조회
        BigDecimal customerPointBalance = customerService.getCustomerPointBalance(customerId);

        // 응답 정보 반환
        customerModel = CustomerModel.create(customerModel.customerId(), customerModel.customerName(), customerPointBalance);
        return new CustomerInfo(customerModel);
    }

    /**
     * 고객 포인트 충전
     * @param customerId 고객_ID
     * @param pointAmount 포인트 금액
     * @return 고객 포인트 충전 내역
     */
    public CustomerInfoWithCustomerPointHistory chargeCustomerPoint(Long customerId, BigDecimal pointAmount) {
        if (customerId == null) throw new CustomException(ResponseMessage.INVALID, "고객 ID가 존재하지 않습니다.");
        if (pointAmount == null) throw new CustomException(ResponseMessage.INVALID, "충전 금액이 존재하지 않습니다.");

        // 고객 확인
        CustomerModel customerModel;
        // 고객 조회
        customerModel = customerService.getAvailableCustomer(customerId);
        // 고객 검증
        customerValidator.checkIfCustomerValid(customerModel);

        // 충전 포인트 확인
        CustomerPointHistoryModel customerPointHistoryModel;
        customerPointHistoryModel = CustomerPointHistoryModel.create(customerId, pointAmount, PointType.CHARGE, null);
        // 충전 포인트 검증
        customerValidator.checkIfPointValidBeforeCharge(customerPointHistoryModel);

        // 포인트 충전
        CustomerPointHistoryModel savedCustomerPointHistoryModel;
        savedCustomerPointHistoryModel = customerService.saveCustomerPointHistory(customerPointHistoryModel);

        // 고객 포인트 잔액 조회
        BigDecimal customerPointBalance;
        customerPointBalance = customerService.getCustomerPointBalance(customerId);

        // 응답 정보 반환
        customerModel = CustomerModel.create(customerModel.customerId(), customerModel.customerName(), customerPointBalance);
        return new CustomerInfoWithCustomerPointHistory(customerModel, savedCustomerPointHistoryModel);
    }
}
