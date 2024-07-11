package com.io.hhplus.concert.infrastructure.reservation.entity;

import com.io.hhplus.concert.common.enums.ReceiveMethod;
import com.io.hhplus.concert.common.enums.ReservationStatus;
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
@Table(name = "RESERVATION")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "CUSTOMER_ID", nullable = false, updatable = false)
    private Long customerId;

    @Column(name = "RESERVER_NAME", nullable = false, updatable = false, length = 100)
    private String reserverName;

    @Column(name = "STATUS", nullable = false, length = 50)
    private ReservationStatus reservationStatus;

    @Column(name = "STATUS_CHANGED_AT", nullable = false)
    private Date reservationStatusChangedAt;

    @Column(name = "RECEIVE_METHOD", nullable = false, length = 50)
    private ReceiveMethod receiveMethod;

    @Column(name = "RECEIVER_NAME", nullable = false, length = 100)
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
}
