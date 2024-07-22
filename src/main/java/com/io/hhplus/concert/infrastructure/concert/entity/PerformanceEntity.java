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
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "CONCERT_ID", nullable = false, updatable = false)
    private Long concertId;

    @Column(name = "PERFORMANCE_PRICE", nullable = false, precision = 18, scale = 3)
    private BigDecimal performancePrice;

    @Column(name = "CAPACITY_LIMIT", nullable = false)
    private Integer capacityLimit;

    @Column(name = "PERFORMED_AT", nullable = false)
    private Date performedAt;

    @Builder.Default
    @Embedded
    private AuditSection auditSection = new AuditSection();

    @Column(name = "DELETED_AT", nullable = true)
    private Date deletedAt;

    public static PerformanceEntity from(Performance performance) {
        return PerformanceEntity.builder()
                .id(performance.performanceId())
                .concertId(performance.concertId())
                .performancePrice(performance.performancePrice())
                .capacityLimit(performance.capacityLimit())
                .performedAt(performance.performedAt())
                .deletedAt(performance.deletedAt())
                .build();
    }

    public Performance toDomain() {
        return Performance.builder()
                .performanceId(this.id)
                .concertId(this.concertId)
                .performancePrice(this.performancePrice)
                .capacityLimit(this.capacityLimit)
                .performedAt(this.performedAt)
                .createdAt(this.auditSection.getCreatedAt())
                .modifiedAt(this.auditSection.getModifiedAt())
                .deletedAt(this.deletedAt)
                .build();
    }
}
