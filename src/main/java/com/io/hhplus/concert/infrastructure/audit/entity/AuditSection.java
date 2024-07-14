package com.io.hhplus.concert.infrastructure.audit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 공통 audit entity 구현
 * @link <a href="https://stackoverflow.com/questions/221611/creation-timestamp-and-last-update-timestamp-with-hibernate-and-mysql">Using JPA @EntityListeners</a>
 */
@Getter
@Setter
@Embeddable
public class AuditSection {
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFIED_AT", nullable = false)
    private Date modifiedAt;
}