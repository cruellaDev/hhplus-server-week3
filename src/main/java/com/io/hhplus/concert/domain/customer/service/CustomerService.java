package com.io.hhplus.concert.domain.customer.service;

import com.io.hhplus.concert.common.enums.PointType;
import com.io.hhplus.concert.domain.customer.model.Customer;
import com.io.hhplus.concert.domain.customer.model.CustomerPointHistory;
import com.io.hhplus.concert.domain.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    /**
     * 고객 조회
     * @param customerId 고객_ID
     * @return 고객 정보
     */
    public Customer getAvailableCustomer(Long customerId) {
        return customerRepository.findAvailableOneById(customerId).orElseGet(Customer::noContents);
    }

    /**
     * 유효 고객 검증
     * @param customer 고객
     * @return 유효 여부
     */
    public boolean meetsIfCustomerValid(Customer customer) {
        return customer != null
                && Customer.isAvailableCustomerId(customer.customerId())
                && Customer.isAvailableCustomerName(customer.customerName());
    }

    /**
     * 고객 포인트 잔액 조회
     * @param customerId 고객 ID
     * @return 포인트잔액
     */
    public BigDecimal getCustomerPointBalance(Long customerId) {
        return customerRepository.sumCustomerPointBalanceByCustomerId(customerId);
    }

    /**
     * 고객 포인트 충전 정보 검증
     * @param customerPointHistory 고객포인트내역 정보
     * @return 고객 포인트 충전 유효 여부
     */
    public boolean meetsIfPointValidBeforeCharge(CustomerPointHistory customerPointHistory) {
        return customerPointHistory != null
                && !CustomerPointHistory.isInSufficientPointAmount(customerPointHistory.pointAmount())
                && CustomerPointHistory.isAvailablePointTypeWhenCharge(customerPointHistory.pointType());
    }

    /**
     * 고객 포인트 내역 저장
     * @param customerPointHistory 고객포인트내역 정보
     * @return 고객포인트내역 저장 정보
     */
    public CustomerPointHistory saveCustomerPointHistory(CustomerPointHistory customerPointHistory) {
        return customerRepository.saveCustomerPointHistory(customerPointHistory);
    }

    /**
     * 고객포인트 잔액 충분한지 확인
     * @param customerId 고객_ID
     * @param targetAmount 비교 금액
     * @return 충분할 시 true
     */
    public boolean meetsIfPointBalanceSufficient(Long customerId, BigDecimal targetAmount) {
        return targetAmount != null && getCustomerPointBalance(customerId).compareTo(targetAmount) >= 0;
    }

    /**
     * 고객 포인트 사용
     * @param customerId 고객_ID
     * @param amount 금액
     * @return 고객 포인트 사용 정보
     */
    public CustomerPointHistory useCustomerPoint(Long customerId, BigDecimal amount) {
        CustomerPointHistory customerPointHistory = CustomerPointHistory.create(customerId, amount, PointType.USE, null);
        return saveCustomerPointHistory(customerPointHistory);
    }
}
