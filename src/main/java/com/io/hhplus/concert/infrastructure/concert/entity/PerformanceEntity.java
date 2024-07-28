package com.io.hhplus.concert.infrastructure.concert.entity;

import com.io.hhplus.concert.domain.concert.model.Performance;
import com.io.hhplus.concert.infrastructure.audit.entity.AuditListener;
import com.io.hhplus.concert.infrastructure.audit.entity.AuditSection;
import com.io.hhplus.concert.infrastructure.audit.entity.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "PERFORMANCE")
public class PerformanceEntity implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, updatable = false, columnDefinition = "공연_ID")
    private Long id;

    @Column(name = "CONCERT_ID", nullable = false, updatable = false, columnDefinition = "콘서트_ID")
    private Long concertId;

    @Column(name = "PERFORMED_AT", nullable = false, columnDefinition = "공연_일시")
    private Date performedAt;

    @Builder.Default
    @Embedded
    private AuditSection auditSection = new AuditSection();

    @Column(name = "DELETED_AT", nullable = true, columnDefinition = "삭제_일시")
    private Date deletedAt;

    public static PerformanceEntity from(Performance performance) {
        return PerformanceEntity.builder()
                .id(performance.performanceId())
                .concertId(performance.concertId())
                .performedAt(performance.performedAt())
                .deletedAt(performance.deletedAt())
                .build();
    }

    public Performance toDomain() {
        return Performance.builder()
                .performanceId(this.id)
                .concertId(this.concertId)
                .performedAt(this.performedAt)
                .createdAt(this.auditSection.getCreatedAt())
                .modifiedAt(this.auditSection.getModifiedAt())
                .deletedAt(this.deletedAt)
                .build();
    }
}
