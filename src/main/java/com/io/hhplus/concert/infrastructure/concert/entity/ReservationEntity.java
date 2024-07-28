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

    @Column(name = "RESERVER_NAME", nullable = true, updatable = false, length = 100)
    private String reserverName;

    @Column(name = "RECEIVE_METHOD", nullable = true, length = 50)
    private ReceiveMethod receiveMethod;

    @Column(name = "RECEIVER_NAME", nullable = true, length = 100)
    private String receiverName;

    @Column(name = "RECEIVE_POSTCODE", nullable = true, length = 20)
    private String receivePostcode;

    @Column(name = "RECEIVE_BASE_ADDRESS", nullable = true, length = 100)
    private String receiveBaseAddress;

    @Column(name = "RECEIVE_DETAIL_ADDRESS", nullable = true, length = 300)
    private String receiveDetailAddress;

    @Builder.Default
    @Embedded
    private AuditSection auditSection = new AuditSection();

    @Column(name = "DELETED_AT", nullable = true)
    private Date deletedAt;

    public static ReservationEntity from(Reservation reservation) {
        return ReservationEntity.builder()
                .id(reservation.reservationId())
                .customerId(reservation.customerId())
                .reserverName(reservation.reserverName())
                .receiveMethod(reservation.receiveMethod())
                .receiverName(reservation.receiverName())
                .receivePostcode(reservation.receiverPostcode())
                .receiveBaseAddress(reservation.receiverBaseAddress())
                .receiveDetailAddress(reservation.receiverDetailAddress())
                .deletedAt(reservation.deletedAt())
                .build();
    }

    public Reservation toDomain() {
        return Reservation.builder()
                .reservationId(this.id)
                .customerId(this.customerId)
                .reserverName(this.reserverName)
                .receiveMethod(this.receiveMethod)
                .receiverName(this.receiverName)
                .receiverPostcode(this.receivePostcode)
                .receiverBaseAddress(this.receiveBaseAddress)
                .receiverDetailAddress(this.receiveDetailAddress)
                .createdAt(this.auditSection.getCreatedAt())
                .modifiedAt(this.auditSection.getModifiedAt())
                .deletedAt(this.deletedAt)
                .build();
    }
}
