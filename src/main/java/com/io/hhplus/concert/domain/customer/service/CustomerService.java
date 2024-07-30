package com.io.hhplus.concert.domain.customer.service;

import com.io.hhplus.concert.application.customer.dto.ChargeCustomerPointServiceResponse;
import com.io.hhplus.concert.application.customer.dto.CustomerPointBalanceServiceResponse;
import com.io.hhplus.concert.application.customer.dto.UseCustomerPointServiceResponse;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.domain.customer.model.Customer;
import com.io.hhplus.concert.domain.customer.model.CustomerPointHistory;
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
        Customer customer = customerRepository.findAvailableCustomer(customerId)
                .filter(o
                        -> o.isNotDreamed()
                        && o.isNotWithdrawn()
                        && o.isNotDeleted())
                .orElseThrow(() -> new CustomException(ResponseMessage.CUSTOMER_NOT_FOUND));
        return ChargeCustomerPointServiceResponse.of(
                customerRepository.saveCustomer(customer.chargePoint(chargeAmount)),
                customerRepository.saveCustomerPointHistory(CustomerPointHistory.chargePointOf(customer, chargeAmount))
        );
    }

    /**
     * 고객 포인트 사용
     * @param customerId 고객_ID
     * @param useAmount 충전 금액
     * @return 고객 포인트 사용 정보
     */
    @Transactional
    public UseCustomerPointServiceResponse useCustomerPoint(Long customerId, BigDecimal useAmount) {
        // TODO PESSIMISTIC_WRITE VS PESSIMISTIC_READ(갱신소실 문제 있음!!) 알기
        // 비관적 락도 row 조회 가능하다! 단 select for update는 row 조회 불가
        Customer customer = customerRepository.findAvailableCustomer(customerId)
                .filter(o
                        -> o.isNotDreamed()
                        && o.isNotWithdrawn()
                        && o.isNotDeleted())
                .orElseThrow(() -> new CustomException(ResponseMessage.CUSTOMER_NOT_FOUND));
        return UseCustomerPointServiceResponse.of(
                customerRepository.saveCustomer(customer.usePoint(useAmount)),
                customerRepository.saveCustomerPointHistory(CustomerPointHistory.usePointOf(customer, useAmount)));
    }
}
