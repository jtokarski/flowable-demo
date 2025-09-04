package org.defendev.hibernate.envers.demo.eaa.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Table;
import org.hibernate.envers.ModifiedEntityNames;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import java.util.HashSet;
import java.util.Set;



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

    @ElementCollection
    @JoinTable(
        name = "VersioningRevisionChanges",
        joinColumns = @JoinColumn(name = "idOfVersioningRevision")
    )
    @Column(name = "entityName")
    @ModifiedEntityNames
    private Set<String> modifiedEntityNames = new HashSet<>();

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
}
