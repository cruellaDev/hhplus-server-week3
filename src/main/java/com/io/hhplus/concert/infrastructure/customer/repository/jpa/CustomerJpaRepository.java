package com.io.hhplus.concert.infrastructure.customer.repository.jpa;

import com.io.hhplus.concert.infrastructure.customer.entity.CustomerEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT C FROM CustomerEntity C WHERE C.id =: customerId AND C.dreamedAt IS NULL AND C.withdrawnAt IS NULL AND C.deletedAt IS NULL")
    Optional<CustomerEntity> findByIdWithPessimisticLock(@Param("customerId") Long customerId);
}
