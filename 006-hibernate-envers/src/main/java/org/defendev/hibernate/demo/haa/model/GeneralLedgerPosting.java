package org.defendev.hibernate.demo.haa.model;

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

import static java.util.Objects.nonNull;



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
        /*
         * The nonNull(id) is additional condition on top what was auto-generated. It is important
         * and necessary in order to be able - in single database transaction - to add more than
         * one element to the Set.
         *
         */
        return nonNull(id) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
