package org.defendev.hibernate.envers.demo.eaa.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;



@Table(name = "FinancialTransaction")
@Entity
public class FinancialTransaction {

    public enum LifecycleStatus {
        NEW, APPROVED, POSTED;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Id
    private Long id;

    @Audited
    @Column(name = "recordedBy", nullable = false)
    private String recordedBy;

    @Audited
    @Column(name = "memo", nullable = false)
    private String memo;

    @Audited
    @Column(name = "transactionDateTimeZulu")
    private LocalDateTime transactionDateTimeZulu;

    @Audited
    @Column(name = "lifecycleStatus")
    @Enumerated(EnumType.STRING)
    private LifecycleStatus lifecycleStatus;

    @OneToMany(mappedBy = "financialTransaction", orphanRemoval = true, cascade = {CascadeType.PERSIST})
    private Set<GeneralLedgerPosting> generalLedgerPostings = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecordedBy() {
        return recordedBy;
    }

    public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public LocalDateTime getTransactionDateTimeZulu() {
        return transactionDateTimeZulu;
    }

    public void setTransactionDateTimeZulu(LocalDateTime transactionDateTimeZulu) {
        this.transactionDateTimeZulu = transactionDateTimeZulu;
    }

    public LifecycleStatus getLifecycleStatus() {
        return lifecycleStatus;
    }

    public void setLifecycleStatus(LifecycleStatus lifecycleStatus) {
        this.lifecycleStatus = lifecycleStatus;
    }

    public Set<GeneralLedgerPosting> getGeneralLedgerPostings() {
        return generalLedgerPostings;
    }

    public void setGeneralLedgerPostings(Set<GeneralLedgerPosting> generalLedgerPostings) {
        this.generalLedgerPostings = generalLedgerPostings;
    }
}
