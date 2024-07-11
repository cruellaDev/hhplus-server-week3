package com.io.hhplus.concert.infrastructure.audit.entity;

/**
 * @link <a href="https://stackoverflow.com/questions/221611/creation-timestamp-and-last-update-timestamp-with-hibernate-and-mysql">Using JPA @EntityListeners</a>
 */
public interface Auditable {
    AuditSection getAuditSection();
    void setAuditSection(AuditSection audit);
}
