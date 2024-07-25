package com.io.hhplus.concert.domain.customer.service;

import com.io.hhplus.concert.application.customer.dto.ChargeCustomerPointServiceResponse;
import com.io.hhplus.concert.application.customer.dto.CustomerPointBalanceServiceResponse;
import com.io.hhplus.concert.application.customer.dto.UseCustomerPointServiceResponse;
import com.io.hhplus.concert.common.enums.PointType;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.domain.customer.dto.ChargeCustomerPointServiceRequest;
import com.io.hhplus.concert.domain.customer.dto.UseCustomerPointServiceRequest;
import com.io.hhplus.concert.domain.customer.model.Customer;
import com.io.hhplus.concert.domain.customer.model.CustomerPointHistory;
import com.io.hhplus.concert.domain.customer.service.model.CustomerModel;
import com.io.hhplus.concert.domain.customer.service.model.CustomerPointHistoryModel;
import com.io.hhplus.concert.domain.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    /**
     * 고객 포인트 잔액 조회
     * @param customerId 고객 ID
     * @return 포인트잔액
     */
    public CustomerPointBalanceServiceResponse getCustomerPointBalance(Long customerId) {
        Customer customer = customerRepository.findAvailableCustomer(customerId)
                .filter(o
                        -> o.isNotDreamed()
                        && o.isNotWithdrawn()
                        && o.isNotDeleted())
                .orElseThrow(() -> new CustomException(ResponseMessage.CUSTOMER_NOT_FOUND));
        return CustomerPointBalanceServiceResponse.from(customer);
    }

    /**
     * 고객 포인트 충전
     * @param customerId 고객_ID
     * @param chargeAmount 충전 금액
     * @return 고객 포인트 충전 정보
     */
    @Transactional
    public ChargeCustomerPointServiceResponse chargeCustomerPoint(Long customerId, BigDecimal chargeAmount){
        // 고객 조회
        Customer customer = customerRepository.findAvailableCustomer(customerId)
                .filter(o
                        -> o.isNotDreamed()
                        && o.isNotWithdrawn()
                        && o.isNotDeleted())
                .orElseThrow(() -> new CustomException(ResponseMessage.CUSTOMER_NOT_FOUND));
        // 포인트 update
        Customer updatedCustomer = customerRepository.saveCustomer(customer.chargePoint(chargeAmount));
        // 포인트 내역 insert
        CustomerPointHistory customerPointHistory = customerRepository.saveCustomerPointHistory(CustomerPointHistory.chargePointOf(customer, chargeAmount));
        return ChargeCustomerPointServiceResponse.of(updatedCustomer, customerPointHistory);
    }

    /**
     * 고객 포인트 사용
     * @param customerId 고객_ID
     * @param useAmount 충전 금액
     * @return 고객 포인트 사용 정보
     */
    @Transactional
    public UseCustomerPointServiceResponse useCustomerPoint(Long customerId, BigDecimal useAmount) {
        // 고객 조회
        Customer customer = customerRepository.findAvailableCustomer(customerId)
                .filter(o
                        -> o.isNotDreamed()
                        && o.isNotWithdrawn()
                        && o.isNotDeleted())
                .orElseThrow(() -> new CustomException(ResponseMessage.CUSTOMER_NOT_FOUND));
        // 포인트 update
        Customer updatedCustomer = customerRepository.saveCustomer(customer.usePoint(useAmount));
        // 포인트 내역 insert
        CustomerPointHistory customerPointHistory = customerRepository.saveCustomerPointHistory(CustomerPointHistory.usePointOf(customer, useAmount));
        return UseCustomerPointServiceResponse.of(updatedCustomer, customerPointHistory);
    }
}
