package com.io.hhplus.concert.infrastructure.payment.entity;

import com.io.hhplus.concert.domain.payment.model.Payment;
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
@Table(name = "PAYMENT")
public class PaymentEntity implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "RESERVATION_ID", nullable = false, updatable = false)
    private Long reservationId;

    @Column(name = "PAY_AMOUNT", nullable = false, updatable = false)
    private BigDecimal payAmount;

    @Column(name = "REFUNDABLE_AMOUNT", nullable = false)
    private BigDecimal refundableAmount;

    @Column(name = "REFUND_AMOUNT", nullable = false)
    private BigDecimal refundAmount;

    @Builder.Default
    @Embedded
    private AuditSection auditSection = new AuditSection();

    @Column(name = "DELETED_AT", nullable = true)
    private Date deletedAt;

    public static PaymentEntity from(Payment payment) {
        return PaymentEntity.builder()
                .id(payment.paymentId())
                .reservationId(payment.reservationId())
                .payAmount(payment.payAmount())
                .refundableAmount(payment.refundableAmount())
                .refundAmount(payment.refundAmount())
                .deletedAt(payment.deletedAt())
                .build();
    }

    public Payment toDomain() {
        return Payment.builder()
                .paymentId(this.id)
                .reservationId(this.reservationId)
                .payAmount(this.payAmount)
                .refundableAmount(this.refundableAmount)
                .refundAmount(this.refundAmount)
                .createdAt(this.auditSection.getCreatedAt())
                .modifiedAt(this.auditSection.getModifiedAt())
                .deletedAt(this.deletedAt)
                .build();
    }
}
