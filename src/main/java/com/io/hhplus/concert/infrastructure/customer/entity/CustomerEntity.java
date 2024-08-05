package com.io.hhplus.concert.infrastructure.customer.entity;

import com.io.hhplus.concert.domain.customer.model.Customer;
import com.io.hhplus.concert.infrastructure.audit.entity.AuditListener;
import com.io.hhplus.concert.infrastructure.audit.entity.AuditSection;
import com.io.hhplus.concert.infrastructure.audit.entity.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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
@Table(name = "CUSTOMER")
public class CustomerEntity implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "UUID", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID customerUuid;

    @Column(name = "CUSTOMER_NAME", nullable = false, length = 100)
    private String customerName;

    @Column(name = "POINT_BALANCE", nullable = false, precision = 18, scale = 3)
    private BigDecimal pointBalance;

    @Column(name = "DREAMED_AT", nullable = true)
    private Date dreamedAt;

    @Column(name = "WITHDRAWN_AT", nullable = true)
    private Date withdrawnAt;

    @Builder.Default
    @Embedded
    private AuditSection auditSection = new AuditSection();

    @Column(name = "DELETED_AT", nullable = true)
    private Date deletedAt;


    public boolean isNotDreamed() {
        return this.dreamedAt == null;
    }

    public boolean isNotWithdrawn() {
        return this.withdrawnAt == null;
    }

    public boolean isNotDeleted() {
        return this.deletedAt == null;
    }

    public static CustomerEntity from(Customer customer) {
        return CustomerEntity.builder()
                .id(customer.customerId())
                .customerUuid(customer.customerUuid())
                .customerName(customer.customerName())
                .pointBalance(customer.pointBalance())
                .dreamedAt(customer.dreamedAt())
                .withdrawnAt(customer.withdrawnAt())
                .deletedAt(customer.deletedAt())
                .build();
    }

    public Customer toDomain() {
        return Customer.builder()
                .customerId(this.id)
                .customerUuid(this.customerUuid)
                .customerName(this.customerName)
                .pointBalance(this.pointBalance)
                .dreamedAt(this.dreamedAt)
                .withdrawnAt(this.withdrawnAt)
                .createdAt(this.auditSection.getCreatedAt())
                .modifiedAt(this.auditSection.getModifiedAt())
                .deletedAt(this.deletedAt)
                .build();
    }

}
