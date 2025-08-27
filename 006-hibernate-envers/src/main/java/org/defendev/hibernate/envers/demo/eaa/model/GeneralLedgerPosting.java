package org.defendev.hibernate.envers.demo.eaa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.Objects;



@Table(name = "GeneralLedgerPosting")
@Entity
public class GeneralLedgerPosting {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Id
    private Long id;

    @JoinColumn(name = "idOfFinancialTransaction", nullable = false)
    @ManyToOne(optional = false)
    private FinancialTransaction financialTransaction;

    @Column(name = "generalLedgerAccountNumber")
    private String generalLedgerAccountNumber;

    @Column(name = "debitOrCredit")
    private String debitOrCredit;

    @Column(name = "amount")
    private BigDecimal amount;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final GeneralLedgerPosting that = (GeneralLedgerPosting) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
