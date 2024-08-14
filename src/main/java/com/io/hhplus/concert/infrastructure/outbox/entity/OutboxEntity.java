package com.io.hhplus.concert.infrastructure.outbox.entity;

import com.io.hhplus.concert.domain.outbox.model.Outbox;
import com.io.hhplus.concert.infrastructure.audit.entity.AuditListener;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "OUTBOX")
public class OutboxEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "DOMAIN_TYPE", nullable = false, length = 300)
    private String domainType;

    @Column(name = "EVENT_TYPE", nullable = false, length = 300)
    private String eventType;

    @Column(name = "KEY", nullable = false, length = 300)
    private String key;

    @Column(name = "PAYLOAD", nullable = true, length = 300)
    private String payload;

    @Column(name = "IS_PUBLISHED", nullable = false)
    private Integer isPublished;

    @Column(name = "CREATED_AT", nullable = false)
    private Date createdAt;

    @Column(name = "PUBLISHED_AT", nullable = true)
    private Date publishedAt;

    public Outbox toDomain() {
        return Outbox.builder()
                .outboxId(this.id)
                .domainType(this.domainType)
                .eventType(this.eventType)
                .payload(this.payload)
                .isPublished(this.isPublished == 1)
                .createdAt(this.createdAt)
                .publishedAt(this.publishedAt)
                .build();
    }

    public static OutboxEntity from(Outbox outbox) {
        return OutboxEntity.builder()
                .id(outbox.outboxId())
                .domainType(outbox.domainType())
                .eventType(outbox.eventType())
                .payload(outbox.payload())
                .isPublished(outbox.isPublished() ? 1 : 0)
                .createdAt(outbox.createdAt())
                .publishedAt(outbox.publishedAt())
                .build();
    }
}
