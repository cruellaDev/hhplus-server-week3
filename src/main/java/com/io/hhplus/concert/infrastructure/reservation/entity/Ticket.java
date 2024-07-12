package com.io.hhplus.concert.infrastructure.reservation.entity;

import com.io.hhplus.concert.common.enums.TicketStatus;
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
@Table(name = "TICKET")
public class Ticket  implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "RESERVATION_ID", nullable = false, updatable = false)
    private Long reservationId;

    @Column(name = "CONCERT_ID", nullable = false, updatable = false)
    private Long concertId;

    @Column(name = "PERFORMANCE_ID", nullable = false, updatable = false)
    private Long performanceId;

    @Column(name = "SEAT_ID", nullable = false, updatable = false)
    private Long seatId;

    @Column(name = "CONCERT_NAME", nullable = false, updatable = false, length = 300)
    private String concertName;

    @Column(name = "ARTIST_NAME", nullable = false, updatable = false, length = 100)
    private String artistName;

    @Column(name = "IS_RECEIVE_ONLINE", nullable = false, updatable = false, precision = 1)
    private Integer isReceiveOnline;

    @Column(name = "IS_RECEIVE_ON_SITE", nullable = false, updatable = false, precision = 1)
    private Integer isReceiveOnSite;

    @Column(name = "IS_RECEIVE_BY_POST", nullable = false, updatable = false, precision = 1)
    private Integer isReceiveByPost;

    @Column(name = "price", nullable = false, updatable = false, precision = 18, scale = 3)
    private BigDecimal price;

    @Column(name = "PERFORMED_AT", nullable = false, updatable = false)
    private Date performedAt;

    @Column(name = "SEAT_NO", nullable = false, updatable = false, length = 100)
    private String seatNo;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "STATUS", nullable = false, length = 50)
    private TicketStatus ticketStatus;

    @Column(name = "RESERVED_AT", nullable = true)
    private Date reservedAt;

    @Column(name = "PUBLISHED_AT", nullable = true)
    private Date publishedAt;

    @Column(name = "RECEIVED_AT", nullable = true)
    private Date receivedAt;

    @Column(name = "CANCEL_ACCEPTED_AT", nullable = true)
    private Date cancelAcceptedAt;

    @Column(name = "CANCEL_APPROVED_AT", nullable = true)
    private Date cancelApprovedAt;

    @Builder.Default
    @Embedded
    private AuditSection auditSection = new AuditSection();

    @Column(name = "DELETED_AT", nullable = true)
    private Date deletedAt;
}
