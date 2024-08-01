package com.io.hhplus.concert.infrastructure.customer.repository.jpa;

import com.io.hhplus.concert.infrastructure.customer.entity.CustomerPointHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerPointHistoryJpaRepository extends JpaRepository<CustomerPointHistoryEntity, Long> {
    List<CustomerPointHistoryEntity> findAllByCustomerId(Long customerId);
}
