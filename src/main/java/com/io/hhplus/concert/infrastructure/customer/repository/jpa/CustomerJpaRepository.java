package com.io.hhplus.concert.infrastructure.customer.repository.jpa;

import com.io.hhplus.concert.infrastructure.customer.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByDreamedAtIsNullAndWithdrawAtIsNullAndDeletedAtIsNullAndIdEquals(Long customerId);
}
