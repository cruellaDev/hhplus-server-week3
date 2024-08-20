package com.io.hhplus.concert.infrastructure.outbox.repository.impl;

import com.io.hhplus.concert.domain.outbox.OutboxRepository;
import com.io.hhplus.concert.domain.outbox.model.Outbox;
import com.io.hhplus.concert.infrastructure.outbox.entity.OutboxEntity;
import com.io.hhplus.concert.infrastructure.outbox.repository.jpa.OutboxJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class OutboxRepositoryImpl implements OutboxRepository {

    private final OutboxJpaRepository outboxJpaRepository;

    @Override
    public Outbox save(Outbox outbox) {
        return outboxJpaRepository.save(OutboxEntity.from(outbox)).toDomain();
    }

    @Override
    public Optional<Outbox> findPaidSuccessOutbox(String key) {
        return outboxJpaRepository.findByDomainTypeAndEventTypeAndKeyAndPublishedAtIsNull("PAYMENT", "PAID_SUCCESS", key).map(OutboxEntity::toDomain);
    }

    @Override
    public List<Outbox> findNotPublishedPaidSuccessOutboxes() {
        return outboxJpaRepository.findAllByDomainTypeAndEventTypeAndPublishedAtIsNull("PAYMENT", "PAID_SUCCESS")
                .stream()
                .map(OutboxEntity::toDomain)
                .collect(Collectors.toList());
    }
}
