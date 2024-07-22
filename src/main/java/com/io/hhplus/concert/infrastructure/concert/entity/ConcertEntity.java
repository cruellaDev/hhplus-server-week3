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

    @Column(name = "SALE_BEGIN_AT", nullable = false)
    private Date saleBeginAt;

    @Column(name = "SALE_END_AT", nullable = false)
    private Date saleEndAt;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "CONCERT_STATUS", nullable = false, length = 50)
    private ConcertStatus concertStatus;

    @Column(name = "IS_RECEIVE_ONLINE", nullable = false)
    private Integer isReceiveOnline;

    @Column(name = "IS_RECEIVE_ON_SITE", nullable = false)
    private Integer isReceiveOnSite;

    @Column(name = "IS_RECEIVE_POST", nullable = false)
    private Integer isReceivePost;

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
                .saleBeginAt(concert.saleBeginAt())
                .saleEndAt(concert.saleEndAt())
                .concertStatus(concert.concertStatus())
                .isReceiveOnline(concert.isReceiveOnline() ? 1 : 0)
                .isReceiveOnSite(concert.isReceiveOnSite() ? 1 : 0)
                .isReceivePost(concert.isReceiveOnPost() ? 1 : 0)
                .deletedAt(concert.deletedAt())
                .build();
    }

    public Concert toDomain() {
        return Concert.builder()
                .concertId(this.id)
                .concertName(this.concertName)
                .artistName(this.artistName)
                .saleBeginAt(this.saleBeginAt)
                .saleEndAt(this.saleEndAt)
                .concertStatus(this.concertStatus)
                .isReceiveOnline(this.isReceiveOnline == 1)
                .isReceiveOnSite(this.isReceiveOnSite == 1)
                .isReceiveOnPost(this.isReceivePost == 1)
                .createdAt(this.auditSection.getCreatedAt())
                .modifiedAt(this.auditSection.getModifiedAt())
                .deletedAt(this.deletedAt)
                .build();
    }
}
