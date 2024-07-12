package com.io.hhplus.concert.infrastructure.customer.entity;

import com.io.hhplus.concert.common.enums.PointType;
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
@Builder
@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "CUSTOMER_POINT_HISTORY")
public class CustomerPointHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "CUSTOMER_ID", nullable = false)
    private Long customerId;

    @Column(name = "AMOUNT", nullable = false, precision = 18, scale = 3)
    private BigDecimal pointAmount;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "TYPE", nullable = false, length = 50)
    private PointType pointType;

    @Builder.Default
    @Embedded
    private AuditSection auditSection = new AuditSection();

    @Column(name = "DELETED_AT", nullable = true)
    private Date deletedAt;

}
