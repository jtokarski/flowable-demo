package org.defendev.flowable.demo.multipoc.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Table;
import org.defendev.flowable.demo.multipoc.config.envers.DefendevAccountingRevisionListener;
import org.hibernate.envers.ModifiedEntityNames;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;



@RevisionEntity(DefendevAccountingRevisionListener.class)
@Table(name = "EnversRevision", schema = "amp_core")
@Entity
public class EnversRevision {

    @RevisionNumber
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Id
    private Long id;

    @RevisionTimestamp
    @Column(name = "unixEpochTimestamp", nullable = false)
    private Long unixEpochTimestamp;

    @ModifiedEntityNames
    @ElementCollection
    @JoinTable(
        name = "EnversRevisionChanges",
        schema = "amp_core",
        joinColumns = @JoinColumn(name = "idOfEnversRevision")
    )
    @Column(name = "entityName")
    private Set<String> modifiedEntityNames = new HashSet<>();

    @Column(name = "checkpointType", nullable = true)
    private String checkpointType;

    @Column(name = "userId", nullable = true)
    private String userId;

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

    public Set<String> getModifiedEntityNames() {
        return modifiedEntityNames;
    }

    public void setModifiedEntityNames(Set<String> modifiedEntityNames) {
        this.modifiedEntityNames = modifiedEntityNames;
    }

    public String getCheckpointType() {
        return checkpointType;
    }

    public void setCheckpointType(String checkpointType) {
        this.checkpointType = checkpointType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Instant getTimestampInstant() {
        // todo
        return null;
    }

    public ZonedDateTime getTimestampZonedZulu() {
        // todo
        return null;
    }

    public LocalDateTime getTimestampLocalZulu() {
        // todo
        return null;
    }

}
