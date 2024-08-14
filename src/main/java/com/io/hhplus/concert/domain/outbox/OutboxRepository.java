package com.io.hhplus.concert.domain.outbox;

import com.io.hhplus.concert.domain.outbox.model.Outbox;

import java.util.List;
import java.util.Optional;

public interface OutboxRepository {
    Outbox save(Outbox outbox);
    Optional<Outbox> findPaidSuccessOutbox(String key);
    List<Outbox> findNotPublishedPaidSuccessOutboxes();
}
