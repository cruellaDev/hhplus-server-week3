package com.io.hhplus.concert.domain.customer.repository;

import com.io.hhplus.concert.common.enums.PointType;
import com.io.hhplus.concert.domain.customer.model.Customer;
import com.io.hhplus.concert.domain.customer.model.CustomerPointHistory;

import java.math.BigDecimal;
import java.util.Optional;

public interface CustomerRepository {
    Optional<Customer> findAvailableOneById(Long customerId);
    BigDecimal sumCustomerPointBalanceByCustomerId(Long customerId);
    Optional<Customer> findAvailableOneWithPointBalanceById(Long customerId);
    CustomerPointHistory saveCustomerPointHistory(CustomerPointHistory customerPointHistory);
}
