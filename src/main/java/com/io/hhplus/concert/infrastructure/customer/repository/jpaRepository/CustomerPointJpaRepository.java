package com.io.hhplus.concert.infrastructure.customer.repository.jpaRepository;

import com.io.hhplus.concert.infrastructure.customer.entity.CustomerPointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerPointJpaRepository extends JpaRepository<CustomerPointHistory, Long> {
    List<CustomerPointHistory> findAllByCustomerId(Long customerId);
}
