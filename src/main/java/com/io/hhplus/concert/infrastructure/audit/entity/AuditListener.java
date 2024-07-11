package com.io.hhplus.concert.infrastructure.audit.entity;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * 생성_일시, 수정_일시 JPA Auditing 이용하여 시간 매핑
 * @link <a href="https://stackoverflow.com/questions/221611/creation-timestamp-and-last-update-timestamp-with-hibernate-and-mysql">Using JPA @EntityListeners</a>
 */
public class AuditListener {
    @PrePersist
    public void onSave(Object o) {
        if (o instanceof Auditable audit) {
            AuditSection auditSection = audit.getAuditSection();

            if (auditSection == null) {
                auditSection = new AuditSection();
            }

            Timestamp ts = Timestamp.valueOf(LocalDateTime.now());

            if (auditSection.getCreatedAt() == null) {
                auditSection.setCreatedAt(ts);
            }

            auditSection.setModifiedAt(ts);

            audit.setAuditSection(auditSection);
        }
    }

    @PreUpdate
    public void onUpdate(Object o) {
        if (o instanceof Auditable audit) {
            AuditSection auditSection = audit.getAuditSection();

            if (auditSection == null) {
                auditSection = new AuditSection();
            }

            Timestamp ts = Timestamp.valueOf(LocalDateTime.now());
            auditSection.setModifiedAt(ts);

            audit.setAuditSection(auditSection);
        }
    }
}
