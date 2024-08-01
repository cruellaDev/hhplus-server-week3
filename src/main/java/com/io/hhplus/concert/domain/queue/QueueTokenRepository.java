package com.io.hhplus.concert.domain.queue;


import com.io.hhplus.concert.domain.queue.model.QueueToken;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QueueTokenRepository {
    Optional<QueueToken> findLatestQueueToken(Long customerId);
    Optional<QueueToken> findQueueToken(UUID queueToken);
    Long countActiveQueueToken();
    QueueToken saveQueueToken(QueueToken queueToken);
    Optional<QueueToken> findFirstWaitingQueueToken();
    Optional<QueueToken> findLastWaitingQueueToken();
    List<QueueToken> findUpcomingActiveQueueTokens(Pageable pageable);
    List<QueueToken> findUpcomingExpiredQueueTokens(Pageable pageable);
    List<QueueToken> saveAllQueueToken(List<QueueToken> queueTokens);
    Long countUpcomingExpiredToken(Date expirationDate);
}
