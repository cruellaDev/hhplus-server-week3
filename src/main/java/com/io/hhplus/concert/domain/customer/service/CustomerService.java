package com.io.hhplus.concert.domain.customer.service;

import com.io.hhplus.concert.common.enums.PointType;
import com.io.hhplus.concert.domain.customer.service.model.CustomerModel;
import com.io.hhplus.concert.domain.customer.service.model.CustomerPointHistoryModel;
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
    public CustomerModel getAvailableCustomer(Long customerId) {
        return customerRepository.findAvailableOneById(customerId).orElseGet(CustomerModel::noContents);
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
     * 고객 포인트 내역 저장
     * @param customerPointHistoryModel 고객포인트내역 정보
     * @return 고객포인트내역 저장 정보
     */
    public CustomerPointHistoryModel saveCustomerPointHistory(CustomerPointHistoryModel customerPointHistoryModel) {
        return customerRepository.saveCustomerPointHistory(customerPointHistoryModel);
    }

    /**
     * 고객 포인트 사용
     * @param customerId 고객_ID
     * @param amount 금액
     * @return 고객 포인트 사용 정보
     */
    public CustomerPointHistoryModel useCustomerPoint(Long customerId, BigDecimal amount) {
        CustomerPointHistoryModel customerPointHistoryModel = CustomerPointHistoryModel.create(customerId, amount, PointType.USE, null);
        return saveCustomerPointHistory(customerPointHistoryModel);
    }
}
