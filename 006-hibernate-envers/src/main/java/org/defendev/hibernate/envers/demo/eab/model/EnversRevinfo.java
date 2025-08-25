package org.defendev.hibernate.envers.demo.eab.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;



@Table(name = "EnversRevinfo")
@RevisionEntity
@Entity
public class EnversRevinfo {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @RevisionNumber
    @Id
    private Long id;

    @RevisionTimestamp
    private Long revisionTimestamp;

    /*
     * Consider additionally: @ModifiedEntityNames
     *   See: TrackingModifiedEntitiesRevisionMapping
     *
     */

}
