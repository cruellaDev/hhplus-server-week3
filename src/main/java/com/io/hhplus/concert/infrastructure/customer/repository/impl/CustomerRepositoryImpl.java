package com.io.hhplus.concert.infrastructure.customer.repository.impl;

import com.io.hhplus.concert.common.enums.PointType;
import com.io.hhplus.concert.domain.customer.model.Customer;
import com.io.hhplus.concert.domain.customer.model.CustomerPointHistory;
import com.io.hhplus.concert.domain.customer.repository.CustomerRepository;
import com.io.hhplus.concert.infrastructure.customer.repository.jpaRepository.CustomerJpaRepository;
import com.io.hhplus.concert.infrastructure.customer.repository.jpaRepository.CustomerPointJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerPointJpaRepository customerPointJpaRepository;

    private boolean isNotDreamed(Date dreamedAt) {
        return dreamedAt == null;
    }

    private boolean isNotWithdraw(Date withdrawAt) {
        return withdrawAt == null;
    }

    private boolean isNotDeleted(Date deletedAt) {
        return deletedAt == null;
    }

    private BigDecimal convertPositiveOrNegative(PointType pointType, BigDecimal pointAmount) {
        if (pointType == null) {
            return BigDecimal.ZERO;
        }
        return pointType.equals(PointType.USE) ? pointAmount.negate() : pointAmount;
    }

    private Customer mapEntityToDto(com.io.hhplus.concert.infrastructure.customer.entity.Customer entity, BigDecimal pointAmount) {
        return Customer.create(entity.getId(), entity.getCustomerName(), pointAmount);
    }

    private CustomerPointHistory mapEntityToDto(com.io.hhplus.concert.infrastructure.customer.entity.CustomerPointHistory entity) {
        return CustomerPointHistory.create(entity.getCustomerId(), entity.getPointAmount(), entity.getPointType(), entity.getAuditSection().getCreatedAt());
    }

    @Override
    public Optional<Customer> findAvailableOneById(Long customerId) {
        return customerJpaRepository.findById(customerId)
                .filter(customerEntity -> isNotDreamed(customerEntity.getDreamedAt())
                        && isNotWithdraw(customerEntity.getWithdrawAt())
                        && isNotDeleted(customerEntity.getDeletedAt()))
                .map(entity -> mapEntityToDto(entity, BigDecimal.ZERO));
    }

    @Override
    public BigDecimal sumCustomerPointBalanceByCustomerId(Long customerId) {
        return customerPointJpaRepository.findAllByCustomerId(customerId)
                .stream()
                .filter(pointEntity -> isNotDeleted(pointEntity.getDeletedAt()))
                .map(pointEntity -> convertPositiveOrNegative(pointEntity.getPointType(), pointEntity.getPointAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Optional<Customer> findAvailableOneWithPointBalanceById(Long customerId) {
        return customerJpaRepository.findById(customerId)
                .filter(customerEntity -> isNotDreamed(customerEntity.getDreamedAt())
                        && isNotWithdraw(customerEntity.getWithdrawAt())
                        && isNotDeleted(customerEntity.getDeletedAt()))
                .map(customerEntity -> {
                    BigDecimal pointBalance = sumCustomerPointBalanceByCustomerId(customerId);
                    return mapEntityToDto(customerEntity, pointBalance);
                });
    }

    @Override
    public CustomerPointHistory saveCustomerPointHistory(CustomerPointHistory customerPointHistory) {
        com.io.hhplus.concert.infrastructure.customer.entity.CustomerPointHistory entity
                = com.io.hhplus.concert.infrastructure.customer.entity.CustomerPointHistory.builder()
                .customerId(customerPointHistory.customerId())
                .pointType(customerPointHistory.pointType())
                .pointAmount(customerPointHistory.pointAmount())
                .build();
        com.io.hhplus.concert.infrastructure.customer.entity.CustomerPointHistory saved
                = customerPointJpaRepository.save(entity);
        return mapEntityToDto(saved);
    }
}
