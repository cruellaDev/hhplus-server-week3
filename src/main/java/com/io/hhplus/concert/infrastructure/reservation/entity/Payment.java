package com.io.hhplus.concert.infrastructure.reservation.entity;

import com.io.hhplus.concert.common.enums.PayMethod;
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
@Table(name = "PAYMENT")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "RESERVATION_ID", nullable = false, updatable = false)
    private Long reservationId;

    @Column(name = "PAY_METHOD", nullable = false, updatable = false)
    private PayMethod payMethod;

    @Column(name = "PAY_AMOUNT", nullable = false, updatable = false)
    private BigDecimal payAmount;

    @Column(name = "REFUNDABLE_AMOUNT", nullable = false)
    private BigDecimal refundableAmount;

    @Column(name = "REFUND_AMOUNT", nullable = false)
    private BigDecimal refundAmount;

    @Column(name = "DELIVERY_AMOUNT", nullable = false)
    private BigDecimal deliveryAmount;

    @Column(name = "DELIVERY_DISCOUNT_AMOUNT", nullable = false)
    private BigDecimal deliveryDiscountAmount;

    @Column(name = "DELIVERY_DISCOUNT_REASON", nullable = false)
    private String deliveryDiscountReason;

    @Builder.Default
    @Embedded
    private AuditSection auditSection = new AuditSection();

    @Column(name = "DELETED_AT", nullable = true)
    private Date deletedAt;
}
