package org.defendev.hibernate.envers.demo.eaa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;



@RevisionEntity
@Table(name = "VersioningRevision")
@Entity
public class VersioningRevision {

    @RevisionNumber
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Id
    private Long id;

    @RevisionTimestamp
    @Column(name = "unixEpochTimestamp", nullable = false)
    private Long unixEpochTimestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUnixEpochTimestamp() {
        return unixEpochTimestamp;
    }

    public void setUnixEpochTimestamp(Long unixEpochTimestamp) {
        this.unixEpochTimestamp = unixEpochTimestamp;
    }
}
