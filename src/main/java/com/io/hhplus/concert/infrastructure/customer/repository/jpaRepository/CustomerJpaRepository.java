package com.io.hhplus.concert.infrastructure.customer.repository.jpaRepository;

import com.io.hhplus.concert.domain.customer.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Long> {
}
