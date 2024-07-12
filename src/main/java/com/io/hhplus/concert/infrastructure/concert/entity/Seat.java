package com.io.hhplus.concert.infrastructure.concert.entity;

import com.io.hhplus.concert.common.enums.SeatStatus;
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
@Table(name = "SEAT")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "PERFORMANCE_ID", nullable = false, updatable = false)
    private Long performanceId;

    @Column(name = "CONCERT_ID", nullable = false, updatable = false)
    private Long concertId;

    @Column(name = "SEAT_NO", nullable = false, length = 100)
    private String seatNo;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="STATUS", nullable = false, length = 50)
    private SeatStatus seatStatus;

    @Builder.Default
    @Embedded
    private AuditSection auditSection = new AuditSection();

    @Column(name = "DELETED_AT", nullable = true)
    private Date deletedAt;
}
