package com.io.hhplus.concert.infrastructure.customer.entity;

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
@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "CUSTOMER")
public class Customer implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 100)
    private String customerName;

    @Column(name = "DREAMED_AT", nullable = true)
    private Date dreamedAt;

    @Column(name = "WITHDRAW_AT", nullable = true)
    private Date withdrawAt;

    @Embedded
    private AuditSection auditSection = new AuditSection();

    @Column(name = "DELETED_AT", nullable = true)
    private Date deletedAt;
}
