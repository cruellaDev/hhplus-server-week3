package com.io.hhplus.concert.infrastructure.concert.entity;

import com.io.hhplus.concert.common.enums.ConcertStatus;
import com.io.hhplus.concert.domain.concert.model.Concert;
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
@Table(name = "CONCERT")
public class ConcertEntity implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "CONCERT_NAME", nullable = false, length = 300)
    private String concertName;

    @Column(name = "ARTIST_NAME", nullable = false, length = 100)
    private String artistName;

    @Column(name = "BOOK_BEGIN_AT", nullable = false)
    private Date bookBeginAt;

    @Column(name = "BOOK_END_AT", nullable = false)
    private Date bookEndAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "CONCERT_STATUS", nullable = false, length = 50)
    private ConcertStatus concertStatus;

    @Builder.Default
    @Embedded
    private AuditSection auditSection = new AuditSection();

    @Column(name = "DELETED_AT", nullable = true)
    private Date deletedAt;

    public static ConcertEntity from(Concert concert) {
        return ConcertEntity.builder()
                .id(concert.concertId())
                .concertName(concert.concertName())
                .artistName(concert.artistName())
                .bookBeginAt(concert.bookBeginAt())
                .bookEndAt(concert.bookEndAt())
                .concertStatus(concert.concertStatus())
                .deletedAt(concert.deletedAt())
                .build();
    }

    public Concert toDomain() {
        return Concert.builder()
                .concertId(this.id)
                .concertName(this.concertName)
                .artistName(this.artistName)
                .bookBeginAt(this.bookBeginAt)
                .bookEndAt(this.bookEndAt)
                .concertStatus(this.concertStatus)
                .createdAt(this.auditSection.getCreatedAt())
                .modifiedAt(this.auditSection.getModifiedAt())
                .deletedAt(this.deletedAt)
                .build();
    }
}
