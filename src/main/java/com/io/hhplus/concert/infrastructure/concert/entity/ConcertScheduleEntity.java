package com.io.hhplus.concert.infrastructure.concert.entity;

import com.io.hhplus.concert.domain.concert.model.ConcertSchedule;
import com.io.hhplus.concert.infrastructure.audit.entity.AuditListener;
import com.io.hhplus.concert.infrastructure.audit.entity.AuditSection;
import com.io.hhplus.concert.infrastructure.audit.entity.Auditable;
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
@Table(name = "CONCERT_SCHEDULE")
public class ConcertScheduleEntity implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "CONCERT_ID", nullable = false, updatable = false)
    private Long concertId;

    @Column(name = "PERFORMED_AT", nullable = false)
    private Date performedAt;

    @Builder.Default
    @Embedded
    private AuditSection auditSection = new AuditSection();

    @Column(name = "DELETED_AT", nullable = true)
    private Date deletedAt;

    public static ConcertScheduleEntity from(ConcertSchedule concertSchedule) {
        return ConcertScheduleEntity.builder()
                .id(concertSchedule.concertScheduleId())
                .concertId(concertSchedule.concertId())
                .performedAt(concertSchedule.performedAt())
                .deletedAt(concertSchedule.deletedAt())
                .build();
    }

    public ConcertSchedule toDomain() {
        return ConcertSchedule.builder()
                .concertScheduleId(this.id)
                .concertId(this.concertId)
                .performedAt(this.performedAt)
                .createdAt(this.auditSection.getCreatedAt())
                .modifiedAt(this.auditSection.getModifiedAt())
                .deletedAt(this.deletedAt)
                .build();
    }

    public boolean isEqualConcertId(Long concertId) {
        return concertId != null && this.concertId.compareTo(concertId) == 0;
    }
}
