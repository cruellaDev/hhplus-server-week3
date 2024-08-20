package com.io.hhplus.concert.infrastructure.outbox.repository.jpa;

import com.io.hhplus.concert.infrastructure.outbox.entity.OutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OutboxJpaRepository extends JpaRepository<OutboxEntity, Long> {
    Optional<OutboxEntity> findByDomainTypeAndEventTypeAndKeyAndPublishedAtIsNull(String domainType, String eventType, String key);
    List<OutboxEntity> findAllByDomainTypeAndEventTypeAndPublishedAtIsNull(String domainType, String eventType);
}
