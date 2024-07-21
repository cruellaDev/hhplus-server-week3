package com.io.hhplus.concert.infrastructure.customer.repository.impl;

import com.io.hhplus.concert.common.enums.PointType;
import com.io.hhplus.concert.domain.customer.entity.CustomerEntity;
import com.io.hhplus.concert.domain.customer.entity.CustomerPointHistoryEntity;
import com.io.hhplus.concert.domain.customer.service.model.CustomerModel;
import com.io.hhplus.concert.domain.customer.service.model.CustomerPointHistoryModel;
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

    private CustomerModel mapEntityToDto(CustomerEntity entity, BigDecimal pointAmount) {
        return CustomerModel.create(entity.getId(), entity.getCustomerName(), pointAmount);
    }

    private CustomerPointHistoryModel mapEntityToDto(CustomerPointHistoryEntity entity) {
        return CustomerPointHistoryModel.create(entity.getCustomerId(), entity.getPointAmount(), entity.getPointType(), entity.getAuditSection().getCreatedAt());
    }

    @Override
    public Optional<CustomerModel> findAvailableOneById(Long customerId) {
        return customerJpaRepository.findById(customerId)
                .filter(customerEntityEntity -> isNotDreamed(customerEntityEntity.getDreamedAt())
                        && isNotWithdraw(customerEntityEntity.getWithdrawAt())
                        && isNotDeleted(customerEntityEntity.getDeletedAt()))
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
    public Optional<CustomerModel> findAvailableOneWithPointBalanceById(Long customerId) {
        return customerJpaRepository.findById(customerId)
                .filter(customerEntityEntity -> isNotDreamed(customerEntityEntity.getDreamedAt())
                        && isNotWithdraw(customerEntityEntity.getWithdrawAt())
                        && isNotDeleted(customerEntityEntity.getDeletedAt()))
                .map(customerEntityEntity -> {
                    BigDecimal pointBalance = sumCustomerPointBalanceByCustomerId(customerId);
                    return mapEntityToDto(customerEntityEntity, pointBalance);
                });
    }

    @Override
    public CustomerPointHistoryModel saveCustomerPointHistory(CustomerPointHistoryModel customerPointHistoryModel) {
        CustomerPointHistoryEntity entity
                = CustomerPointHistoryEntity.builder()
                .customerId(customerPointHistoryModel.customerId())
                .pointType(customerPointHistoryModel.pointType())
                .pointAmount(customerPointHistoryModel.pointAmount())
                .build();
        CustomerPointHistoryEntity saved
                = customerPointJpaRepository.save(entity);
        return mapEntityToDto(saved);
    }
}
