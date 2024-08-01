package com.io.hhplus.concert.infrastructure.concert.entity;

import com.io.hhplus.concert.common.enums.ReceiveMethod;
import com.io.hhplus.concert.common.enums.ReservationStatus;
import com.io.hhplus.concert.domain.concert.model.Reservation;
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
@Table(name = "RESERVATION")
public class ReservationEntity implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "CUSTOMER_ID", nullable = false, updatable = false)
    private Long customerId;

    @Column(name = "BOOKER_NAME", nullable = true, updatable = false, length = 100)
    private String bookerName;

    @Builder.Default
    @Embedded
    private AuditSection auditSection = new AuditSection();

    @Column(name = "DELETED_AT", nullable = true)
    private Date deletedAt;

    public static ReservationEntity from(Reservation reservation) {
        return ReservationEntity.builder()
                .id(reservation.reservationId())
                .customerId(reservation.customerId())
                .bookerName(reservation.bookerName())                .deletedAt(reservation.deletedAt())
                .build();
    }

    public Reservation toDomain() {
        return Reservation.builder()
                .reservationId(this.id)
                .customerId(this.customerId)
                .bookerName(this.bookerName)
                .createdAt(this.auditSection.getCreatedAt())
                .modifiedAt(this.auditSection.getModifiedAt())
                .deletedAt(this.deletedAt)
                .build();
    }

    public boolean isEqualCustomerId(Long customerId) {
        return this.customerId != null && this.customerId.compareTo(customerId) == 0;
    }

    public boolean isNotDeleted() {
        return this.deletedAt == null;
    }
}
