package org.defendev.hibernate.envers.demo.eaa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static java.util.Objects.nonNull;



@Table(name = "GeneralLedgerPosting")
@Entity
public class GeneralLedgerPosting {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Id
    private Long id;

    @Audited(withModifiedFlag = true)   // @Audited have to be on both sides of relation. Otherwise - strange errors.
    @JoinColumn(name = "idOfFinancialTransaction", nullable = false)
    @ManyToOne(optional = false)
    private FinancialTransaction financialTransaction;

    @Audited(withModifiedFlag = true)
    @Column(name = "generalLedgerAccountNumber")
    private String generalLedgerAccountNumber;

    @Audited(withModifiedFlag = true)
    @Column(name = "debitOrCredit")
    private String debitOrCredit;

    @Audited(withModifiedFlag = true)
    @Column(name = "amount")
    private BigDecimal amount;

    @Audited(withModifiedFlag = true)
    @Column(name = "lastMarkForAuditZulu")
    private LocalDateTime lastMarkForAuditZulu;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FinancialTransaction getFinancialTransaction() {
        return financialTransaction;
    }

    public void setFinancialTransaction(FinancialTransaction financialTransaction) {
        this.financialTransaction = financialTransaction;
    }

    public String getGeneralLedgerAccountNumber() {
        return generalLedgerAccountNumber;
    }

    public void setGeneralLedgerAccountNumber(String generalLedgerAccountNumber) {
        this.generalLedgerAccountNumber = generalLedgerAccountNumber;
    }

    public String getDebitOrCredit() {
        return debitOrCredit;
    }

    public void setDebitOrCredit(String debitOrCredit) {
        this.debitOrCredit = debitOrCredit;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getLastMarkForAuditZulu() {
        return lastMarkForAuditZulu;
    }

    public void setLastMarkForAuditZulu(LocalDateTime lastMarkForAuditZulu) {
        this.lastMarkForAuditZulu = lastMarkForAuditZulu;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final GeneralLedgerPosting that = (GeneralLedgerPosting) o;
        return nonNull(id) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
