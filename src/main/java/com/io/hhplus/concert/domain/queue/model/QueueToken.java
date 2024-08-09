package com.io.hhplus.concert.domain.queue.model;

import com.io.hhplus.concert.common.GlobalConstants;
import com.io.hhplus.concert.common.enums.QueueStatus;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.common.utils.DateUtils;
import com.io.hhplus.concert.domain.queue.TokenCommand;
import lombok.Builder;

import java.util.Date;
import java.util.UUID;

@Builder
public record QueueToken(
        Long queueTokenId,
        Long customerId,
        UUID queueToken,
        QueueStatus queueStatus,
        Date createdAt,
        Date modifiedAt,
        Date deletedAt
) {
    public boolean isWaiting() {
        return this.queueStatus.isWaiting();
    }

    public boolean isActive() {
        return this.queueStatus.isActive()
                && DateUtils.calculateDuration(DateUtils.getSysDate(), this.modifiedAt) < GlobalConstants.MAX_DURATION_OF_ACTIVE_QUEUE_IN_SECONDS;
    }

    public boolean isNotDeleted() {
        return deletedAt == null;
    }

    public static QueueToken create() {
        return QueueToken.builder()
                .build();
    }

    public QueueToken updateStatusBasedOnCount(Long activeTokenCount) {
        QueueStatus queueStatus = GlobalConstants.MAX_NUMBER_OF_ACTIVE_QUEUE.compareTo(activeTokenCount) >= 0 ? QueueStatus.WAITING : QueueStatus.ACTIVE;
        return QueueToken.builder()
                .customerId(this.customerId)
                .queueToken(this.queueToken)
                .queueStatus(queueStatus)
                .build();
    }

    public static QueueToken enter(TokenCommand.IssueTokenBankCounterCommand command) {
        return QueueToken.builder()
                .customerId(command.getCustomerId())
                .queueToken(UUID.randomUUID())
                .build();
    }

    public QueueToken activate() {
        if (!isWaiting()) throw new CustomException(ResponseMessage.QUEUE_STATUS_INVALID);
        return QueueToken.builder()
                .queueTokenId(this.queueTokenId)
                .customerId(this.customerId)
                .queueToken(this.queueToken)
                .queueStatus(QueueStatus.ACTIVE)
                .build();
    }

    public QueueToken expire() {
        if (!isActive()) throw new CustomException(ResponseMessage.QUEUE_STATUS_INVALID);
        return QueueToken.builder()
                .queueTokenId(this.queueTokenId)
                .customerId(this.customerId)
                .queueToken(this.queueToken)
                .queueStatus(QueueStatus.EXPIRED)
                .build();
    }

    public long calculateWaitingAhead(long firstQueueTokenId) {
        return Math.max(0, firstQueueTokenId - this.queueTokenId);
    }

    public long calculateWaitingBehind(long lastQueueTokenId) {
        return Math.max(0, this.queueTokenId - lastQueueTokenId);
    }

    public static long calculateUpcomingActiveTokenCount(long activeTokenCount) {
        return Math.max(0, GlobalConstants.MAX_NUMBER_OF_ACTIVE_QUEUE - activeTokenCount);
    }
}
