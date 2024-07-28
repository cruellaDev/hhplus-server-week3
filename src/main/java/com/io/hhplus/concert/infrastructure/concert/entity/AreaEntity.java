package com.io.hhplus.concert.infrastructure.concert.entity;

import com.io.hhplus.concert.domain.concert.model.Area;
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
@Table(name = "AREA")
public class AreaEntity implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, updatable = false, columnDefinition = "구역_ID")
    private Long id;

    @Column(name = "PERFORMANCE_ID", nullable = false, updatable = false, columnDefinition = "공연_ID")
    private Long performanceId;

    @Column(name = "CONCERT_ID", nullable = false, updatable = false, columnDefinition = "콘서트_ID")
    private Long concertId;

    @Column(name = "AREA_NAME", nullable = false, length = 100, columnDefinition = "구역_명")
    private String areaName;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "SEAT_PRICE", nullable = false, precision = 18, scale = 3, columnDefinition = "좌석_가격")
    private BigDecimal seatPrice;

    @Column(name = "SEAT_CAPACITY", nullable = false, columnDefinition = "좌석_인원")
    private Long seatCapacity;

    @Builder.Default
    @Embedded
    private AuditSection auditSection = new AuditSection();

    @Column(name = "DELETED_AT", nullable = true, columnDefinition = "삭제_일시")
    private Date deletedAt;

    public static AreaEntity from(Area area) {
        return AreaEntity.builder()
                .id(area.areaId())
                .performanceId(area.performanceId())
                .concertId(area.concertId())
                .areaName(area.areaName())
                .seatPrice(area.seatPrice())
                .seatCapacity(area.seatCapacity())
                .deletedAt(area.deletedAt())
                .build();
    }

    public Area toDomain() {
        return Area.builder()
                .areaId(this.id)
                .performanceId(this.performanceId)
                .concertId(this.concertId)
                .areaName(this.areaName)
                .seatPrice(this.seatPrice)
                .seatCapacity(this.seatCapacity)
                .createdAt(this.auditSection.getCreatedAt())
                .modifiedAt(this.auditSection.getModifiedAt())
                .deletedAt(this.deletedAt)
                .build();
    }

    public boolean isEqualConcertId(Long concertId) {
        return this.concertId.compareTo(concertId) == 0;
    }

    public boolean isEqualPerformanceId(Long performanceId) {
        return this.performanceId.compareTo(performanceId) == 0;
    }

    public boolean isNotDeleted() {
        return this.deletedAt == null;
    }
}
