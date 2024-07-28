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
    @Column(name = "ID", unique = true, nullable = false, updatable = false, columnDefinition = "콘서트_ID")
    private Long id;

    @Column(name = "CONCERT_NAME", nullable = false, length = 300, columnDefinition = "콘서트_명")
    private String concertName;

    @Column(name = "ARTIST_NAME", nullable = false, length = 100, columnDefinition = "아티스트_명")
    private String artistName;

    @Column(name = "BOOK_BEGIN_AT", nullable = false, columnDefinition = "예매_시작_일시")
    private Date bookBeginAt;

    @Column(name = "BOOK_END_AT", nullable = false, columnDefinition = "예매_종료_일시")
    private Date bookEndAt;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "CONCERT_STATUS", nullable = false, length = 50, columnDefinition = "콘서트_상태")
    private ConcertStatus concertStatus;

    @Column(name = "IS_RECEIVE_ONLINE", nullable = false, columnDefinition = "온라인_수령_가능")
    private Integer isReceiveOnline;

    @Column(name = "IS_RECEIVE_ON_SITE", nullable = false, columnDefinition = "현상_수령_가능")
    private Integer isReceiveOnSite;

    @Column(name = "IS_RECEIVE_BY_POST", nullable = false, columnDefinition = "우편_수령_가능")
    private Integer isReceiveByPost;

    @Builder.Default
    @Embedded
    private AuditSection auditSection = new AuditSection();

    @Column(name = "DELETED_AT", nullable = true, columnDefinition = "삭제_일시")
    private Date deletedAt;

    public static ConcertEntity from(Concert concert) {
        return ConcertEntity.builder()
                .id(concert.concertId())
                .concertName(concert.concertName())
                .artistName(concert.artistName())
                .bookBeginAt(concert.bookBeginAt())
                .bookEndAt(concert.bookEndAt())
                .concertStatus(concert.concertStatus())
                .isReceiveOnline(concert.isReceiveOnline() ? 1 : 0)
                .isReceiveOnSite(concert.isReceiveOnSite() ? 1 : 0)
                .isReceiveByPost(concert.isReceiveByPost() ? 1 : 0)
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
                .isReceiveOnline(this.isReceiveOnline == 1)
                .isReceiveOnSite(this.isReceiveOnSite == 1)
                .isReceiveByPost(this.isReceiveByPost == 1)
                .createdAt(this.auditSection.getCreatedAt())
                .modifiedAt(this.auditSection.getModifiedAt())
                .deletedAt(this.deletedAt)
                .build();
    }
}
