package com.io.hhplus.concert.infrastructure.concert.entity;

import com.io.hhplus.concert.infrastructure.audit.entity.AuditListener;
import com.io.hhplus.concert.infrastructure.audit.entity.AuditSection;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "PERFORMANCE")
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "CONCERT_ID", nullable = false, updatable = false)
    private Long concertId;

    @Column(name = "PRICE", nullable = false, precision = 18, scale = 3)
    private BigDecimal concertPrice;

    @Column(name = "CAPACITY_LIMIT", nullable = false)
    private Integer capacityLimit;

    @Column(name = "PERFORMED_AT", nullable = false)
    private Date performedAt;

    @Embedded
    private AuditSection auditSection = new AuditSection();

    @Column(name = "DELETED_AT", nullable = true)
    private Date deletedAt;
}
