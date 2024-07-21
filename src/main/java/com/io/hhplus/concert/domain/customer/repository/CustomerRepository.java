package com.io.hhplus.concert.domain.customer.repository;

import com.io.hhplus.concert.domain.customer.service.model.CustomerModel;
import com.io.hhplus.concert.domain.customer.service.model.CustomerPointHistoryModel;

import java.math.BigDecimal;
import java.util.Optional;

public interface CustomerRepository {
    Optional<CustomerModel> findAvailableOneById(Long customerId);
    BigDecimal sumCustomerPointBalanceByCustomerId(Long customerId);
    Optional<CustomerModel> findAvailableOneWithPointBalanceById(Long customerId);
    CustomerPointHistoryModel saveCustomerPointHistory(CustomerPointHistoryModel customerPointHistoryModel);
}
