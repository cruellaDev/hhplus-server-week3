package com.io.hhplus.concert.infrastructure.concert.entity;

import com.io.hhplus.concert.common.GlobalConstants;
import com.io.hhplus.concert.common.utils.DateUtils;
import com.io.hhplus.concert.domain.concert.model.Ticket;
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
public class TicketEntity implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "RESERVATION_ID", nullable = false, updatable = false)
    private Long reservationId;

    @Column(name = "CONCERT_ID", nullable = false, updatable = false)
    private Long concertId;

    @Column(name = "CONCERT_SCHEDULE_ID", nullable = false, updatable = false)
    private Long concertScheduleId;

    @Column(name = "CONCERT_SEAT_ID", nullable = false, updatable = false)
    private Long concertSeatId;

    @Column(name = "CONCERT_NAME", nullable = false, updatable = false, length = 300)
    private String concertName;

    @Column(name = "ARTIST_NAME", nullable = false, updatable = false, length = 100)
    private String artistName;

    @Column(name = "PERFORMED_AT", nullable = false, updatable = false)
    private Date performedAt;

    @Column(name = "SEAT_NUMBER", nullable = false, length = 300)
    private String seatNumber;

    @Column(name = "TICKET_PRICE", nullable = false, updatable = false, precision = 18, scale = 3)
    private BigDecimal ticketPrice;

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

    public static TicketEntity from(Ticket ticket) {
        return TicketEntity.builder()
                .id(ticket.ticketId())
                .reservationId(ticket.reservationId())
                .concertId(ticket.concertId())
                .concertScheduleId(ticket.concertScheduleId())
                .concertSeatId(ticket.concertSeatId())
                .concertName(ticket.concertName())
                .artistName(ticket.artistName())
                .performedAt(ticket.performedAt())
                .seatNumber(ticket.seatNumber())
                .ticketPrice(ticket.ticketPrice())
                .reservedAt(ticket.reservedAt())
                .publishedAt(ticket.publishedAt())
                .receivedAt(ticket.receivedAt())
                .cancelAcceptedAt(ticket.cancelAcceptedAt())
                .cancelApprovedAt(ticket.cancelApprovedAt())
                .build();
    }

    public Ticket toDomain() {
        return Ticket.builder()
                .ticketId(this.id)
                .reservationId(this.reservationId)
                .concertId(this.concertId)
                .concertScheduleId(this.concertScheduleId)
                .concertSeatId(this.concertSeatId)
                .concertName(this.concertName)
                .artistName(this.artistName)
                .performedAt(this.performedAt)
                .seatNumber(this.seatNumber)
                .ticketPrice(this.ticketPrice)
                .receivedAt(this.receivedAt)
                .publishedAt(this.publishedAt)
                .receivedAt(this.receivedAt)
                .cancelAcceptedAt(this.cancelAcceptedAt)
                .cancelApprovedAt(this.cancelApprovedAt)
                .createdAt(this.auditSection.getCreatedAt())
                .modifiedAt(this.auditSection.getModifiedAt())
                .deletedAt(this.deletedAt)
                .build();
    }

    public boolean isNotDeleted() {
        return this.deletedAt == null;
    }

    public boolean isSeatReserved() {
        return this.reservedAt != null && this.cancelApprovedAt == null;
    }

    public boolean isSeatOccupied() {
        long spentTimeForReservation = DateUtils.calculateDuration(DateUtils.getSysDate(), this.auditSection.getCreatedAt());
        return this.reservedAt == null && spentTimeForReservation < GlobalConstants.MAX_DURATION_OF_ACTIVE_QUEUE_IN_SECONDS;
    }

    public boolean isReservable() {
        if (this.deletedAt != null) return false;
        if (this.reservedAt != null && this.cancelAcceptedAt != null) return false;
        if (this.id != null && this.reservedAt == null) {
            long spentTimeForReservation = DateUtils.calculateDuration(DateUtils.getSysDate(), this.auditSection.getCreatedAt());
            return spentTimeForReservation < GlobalConstants.MAX_DURATION_OF_ACTIVE_QUEUE_IN_SECONDS;
        }
        return true;
    }
}
