package com.io.hhplus.concert.domain.customer.service;

import com.io.hhplus.concert.common.enums.PointType;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.domain.customer.repository.CustomerRepository;
import com.io.hhplus.concert.domain.customer.service.model.CustomerModel;
import com.io.hhplus.concert.domain.customer.service.model.CustomerPointHistoryModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerValidator {

    private final CustomerRepository customerRepository;

    public boolean isAvailableCustomerId(Long customerId) {
        return customerId != null && customerId.compareTo(0L) > 0;
    }

    public boolean isNotAvailableCustomerId(Long customerId) {
        return !this.isAvailableCustomerId(customerId);
    }

    public boolean isAvailableCustomerName(String customerName) {
        return customerName != null && !customerName.isBlank() && !customerName.equals("N/A");
    }

    public boolean isNotAvailableCustomerName(String customerName) {
        return !this.isAvailableCustomerName(customerName);
    }

    public boolean isSufficientPointAmount(BigDecimal pointAmount) {
        return pointAmount == null || pointAmount.compareTo(BigDecimal.ZERO) <= 0;
    }

    public boolean isInsufficientPointAmount(BigDecimal pointAmount) {
        return !this.isSufficientPointAmount(pointAmount);
    }

    public boolean isAvailablePointTypeWhenCharge(PointType pointType) {
        return pointType != null && pointType.equals(PointType.CHARGE);
    }

    public boolean isNotAvailablePointTypeWhenCharge(PointType pointType) {
        return !this.isAvailablePointTypeWhenCharge(pointType);
    }

    /**
     * 고객 유효성 검증
     * @param customerModel 고객 정보
     */
    public void checkIfCustomerValid(CustomerModel customerModel) {
        if (customerModel != null
                && this.isAvailableCustomerId(customerModel.customerId())
                && this.isAvailableCustomerName(customerModel.customerName())) {
            return;
        }
        throw new CustomException(ResponseMessage.NOT_AVAILABLE, "존재하지 않는 고객입니다.");
    }

    /**
     * 고객 포인트 충전 전 검증
     * @param customerPointHistoryModel 고객 포인트 내역
     */
    public void checkIfPointValidBeforeCharge(CustomerPointHistoryModel customerPointHistoryModel) {
        if (customerPointHistoryModel != null
                && this.isInsufficientPointAmount(customerPointHistoryModel.pointAmount())
                && this.isAvailablePointTypeWhenCharge(customerPointHistoryModel.pointType())) {
            return;
        }
        throw new CustomException(ResponseMessage.NOT_AVAILABLE, "충전이 불가능합니다.");
    }

    /**
     * 고객 포인트 잔액 및 차감 금액 비교
     * @param customerId 고객_ID
     * @param targetAmount 비교 금액
     */
    public boolean meetsIfPointBalanceSufficient(Long customerId, BigDecimal targetAmount) {
        BigDecimal customerPointBalance = customerRepository.sumCustomerPointBalanceByCustomerId(customerId);
        return targetAmount != null && customerPointBalance.compareTo(targetAmount) >= 0;
    }

    /**
     * 토큰 검증
     * @param token 토큰 정보
     */
    public void validateCustomer(String token) {
        if (token == null) throw new CustomException(ResponseMessage.INVALID, "토큰 정보가 존재하지 않습니다.");
        Optional<CustomerModel> customerModel = customerRepository.findAvailableOneById(Long.valueOf(token));
        if (customerModel.isEmpty()) throw new CustomException(ResponseMessage.CUSTOMER_NOT_FOUND, "존재하지 않는 고객입니다.");
        if (customerModel.get().customerId().compareTo(0L) <= 0) throw new CustomException(ResponseMessage.CUSTOMER_NOT_FOUND, "존재하지 않는 고객입니다.");
    }
}
