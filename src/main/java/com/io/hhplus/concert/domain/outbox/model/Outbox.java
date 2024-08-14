package com.io.hhplus.concert.domain.outbox.model;

import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.common.utils.DateUtils;
import lombok.Builder;

import java.util.Date;

@Builder
public record Outbox(
        Long outboxId,
        String domainType,
        String eventType,
        String key,
        String payload,
        Boolean isPublished,
        Date createdAt,
        Date publishedAt
) {
    public Outbox publish() {

        if (this.isPublished() || this.publishedAt != null) {
            throw new CustomException(ResponseMessage.ALREADY_PUBLISHED);
        }

        return Outbox.builder()
                .outboxId(this.outboxId)
                .domainType(this.domainType)
                .eventType(this.eventType)
                .key(this.key)
                .payload(this.payload)
                .isPublished(true)
                .createdAt(this.createdAt)
                .publishedAt(DateUtils.getSysDate())
                .build();
    }
}
