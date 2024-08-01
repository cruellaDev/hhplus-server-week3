package com.io.hhplus.concert.infrastructure.customer.repository.impl;

import com.io.hhplus.concert.infrastructure.customer.entity.CustomerEntity;
import com.io.hhplus.concert.infrastructure.customer.entity.CustomerPointHistoryEntity;
import com.io.hhplus.concert.domain.customer.model.Customer;
import com.io.hhplus.concert.domain.customer.model.CustomerPointHistory;
import com.io.hhplus.concert.domain.customer.CustomerRepository;
import com.io.hhplus.concert.infrastructure.customer.repository.jpa.CustomerJpaRepository;
import com.io.hhplus.concert.infrastructure.customer.repository.jpa.CustomerPointHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerPointHistoryJpaRepository customerPointHistoryJpaRepository;

    @Override
    public Optional<Customer> findAvailableCustomer(Long customerId) {
        return customerJpaRepository.findById(customerId)
                .filter(entity -> entity.isNotDreamed()
                                && entity.isNotWithdrawn()
                                && entity.isNotDeleted())
                .map(CustomerEntity::toDomain);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerJpaRepository.save(CustomerEntity.from(customer)).toDomain();
    }

    @Override
    public CustomerPointHistory saveCustomerPointHistory(CustomerPointHistory customerPointHistory) {
        return customerPointHistoryJpaRepository.save(CustomerPointHistoryEntity.from(customerPointHistory)).toDomain();
    }

    @Override
    public Optional<Customer> findAvailableCustomerWithPessimisticLock(Long customerId) {
        return Optional.empty();
    }
}
