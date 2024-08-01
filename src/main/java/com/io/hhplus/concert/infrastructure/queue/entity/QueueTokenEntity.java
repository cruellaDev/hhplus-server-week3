package com.io.hhplus.concert.infrastructure.queue.entity;

import com.io.hhplus.concert.common.enums.QueueStatus;
import com.io.hhplus.concert.domain.queue.model.QueueToken;
import com.io.hhplus.concert.infrastructure.audit.entity.AuditListener;
import com.io.hhplus.concert.infrastructure.audit.entity.AuditSection;
import com.io.hhplus.concert.infrastructure.audit.entity.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "QUEUE_TOKEN")
public class QueueTokenEntity implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "CUSTOMER_ID", nullable = false, updatable = false)
    private Long customerId;

    @Column(name = "TOKEN", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID queueToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "QUEUE_STATUS", nullable = false, length = 50)
    private QueueStatus queueStatus;

    @Builder.Default
    @Embedded
    private AuditSection auditSection = new AuditSection();

    @Column(name = "DELETED_AT", nullable = true)
    private Date deletedAt;

    public static QueueTokenEntity from(QueueToken queueToken) {
        return QueueTokenEntity.builder()
                .id(queueToken.queueTokenId())
                .customerId(queueToken.customerId())
                .queueToken(queueToken.queueToken())
                .queueStatus(queueToken.queueStatus())
                .deletedAt(queueToken.deletedAt())
                .build();
    }

    public QueueToken toDomain() {
        return QueueToken.builder()
                .queueTokenId(this.id)
                .customerId(this.customerId)
                .queueToken(this.queueToken)
                .queueStatus(this.queueStatus)
                .createdAt(this.auditSection.getCreatedAt())
                .modifiedAt(this.auditSection.getModifiedAt())
                .deletedAt(this.deletedAt)
                .build();
    }
}
