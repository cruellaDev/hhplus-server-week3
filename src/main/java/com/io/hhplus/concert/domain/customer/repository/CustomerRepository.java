package com.io.hhplus.concert.domain.customer.repository;

import com.io.hhplus.concert.domain.customer.model.Customer;
import com.io.hhplus.concert.domain.customer.model.CustomerPointHistory;

import java.util.Optional;

public interface CustomerRepository {
    Optional<Customer> findAvailableCustomer(Long customerId);
    Customer saveCustomer(Customer customer);
    CustomerPointHistory saveCustomerPointHistory(CustomerPointHistory customerPointHistory);
}
