package org.defendev.flowable.demo.multipoc.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;



@Table(name = "FinancialTransaction", schema = "amp_core")
@Entity
public class FinancialTransaction {

    public enum LifecycleStatus {
        NEW, APPROVED, POSTED;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Id
    private Long id;

    @Column(name = "lifecycleStatus")
    @Enumerated(EnumType.STRING)
    private LifecycleStatus lifecycleStatus;

    @OneToOne(optional = false, mappedBy = "financialTransaction")
    private MuftpProcess muftpProcess;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LifecycleStatus getLifecycleStatus() {
        return lifecycleStatus;
    }

    public void setLifecycleStatus(LifecycleStatus lifecycleStatus) {
        this.lifecycleStatus = lifecycleStatus;
    }

    public MuftpProcess getMuftpProcess() {
        return muftpProcess;
    }

    public void setMuftpProcess(MuftpProcess muftpProcess) {
        this.muftpProcess = muftpProcess;
    }
}
