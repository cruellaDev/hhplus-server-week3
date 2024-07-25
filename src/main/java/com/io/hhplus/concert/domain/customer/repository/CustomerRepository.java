package com.io.hhplus.concert.domain.customer.repository;

import com.io.hhplus.concert.domain.customer.model.Customer;
import com.io.hhplus.concert.domain.customer.model.CustomerPointHistory;
import com.io.hhplus.concert.domain.customer.service.model.CustomerModel;
import com.io.hhplus.concert.domain.customer.service.model.CustomerPointHistoryModel;

import java.math.BigDecimal;
import java.util.Optional;

public interface CustomerRepository {
    Optional<Customer> findAvailableCustomer(Long customerId);
    Customer saveCustomer(Customer customer);
    CustomerPointHistory saveCustomerPointHistory(CustomerPointHistory customerPointHistory);

    Optional<CustomerModel> findAvailableOneById(Long customerId);
    BigDecimal sumCustomerPointBalanceByCustomerId(Long customerId);
    CustomerPointHistoryModel saveCustomerPointHistory(CustomerPointHistoryModel customerPointHistoryModel);
}
