package com.io.hhplus.concert.domain.concert.entity;

import com.io.hhplus.concert.common.enums.ConcertStatus;
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
@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "CONCERT")
public class ConcertEntity implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 300)
    private String concertName;

    @Column(name = "ARTIST_NAME", nullable = false, length = 100)
    private String artistName;

    @Column(name = "SALE_BEGIN_AT", nullable = false)
    private Date saleBeginAt;

    @Column(name = "SALE_END_AT", nullable = false)
    private Date saleEndAt;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "STATUS", nullable = false, length = 50)
    private ConcertStatus concertStatus;

    @Column(name = "IS_RECEIVE_ONLINE", nullable = false)
    private Integer isReceiveOnline;

    @Column(name = "IS_RECEIVE_ON_SITE", nullable = false)
    private Integer isReceiveOnSite;

    @Column(name = "IS_RECEIVE_POST", nullable = false)
    private int isReceivePost;

    @Embedded
    private AuditSection auditSection = new AuditSection();

    @Column(name = "DELETED_AT", nullable = true)
    private Date deletedAt;
}
