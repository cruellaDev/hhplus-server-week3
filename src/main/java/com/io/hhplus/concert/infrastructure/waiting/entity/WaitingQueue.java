package com.io.hhplus.concert.infrastructure.waiting.entity;

import com.io.hhplus.concert.common.enums.WaitingStatus;
import com.io.hhplus.concert.infrastructure.audit.entity.AuditListener;
import com.io.hhplus.concert.infrastructure.audit.entity.AuditSection;
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
@Table(name = "WAITING_QUEUE")
public class WaitingQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "CUSTOMER_ID", nullable = false, updatable = false)
    private Long customerId;

    @Column(name = "TOKEN", nullable = false, length = 300)
    private String token;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "STATUS", nullable = false, length = 50)
    private WaitingStatus waitingStatus;

    @Builder.Default
    @Embedded
    private AuditSection auditSection = new AuditSection();

    @Column(name = "DELETED_AT", nullable = true)
    private Date deleatedAt;
}
