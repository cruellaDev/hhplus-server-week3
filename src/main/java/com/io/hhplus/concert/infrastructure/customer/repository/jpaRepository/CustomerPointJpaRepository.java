package com.io.hhplus.concert.infrastructure.customer.repository.jpaRepository;

import com.io.hhplus.concert.domain.customer.entity.CustomerPointHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerPointJpaRepository extends JpaRepository<CustomerPointHistoryEntity, Long> {
    List<CustomerPointHistoryEntity> findAllByCustomerId(Long customerId);
}
