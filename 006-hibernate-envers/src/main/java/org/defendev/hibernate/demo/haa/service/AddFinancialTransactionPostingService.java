package org.defendev.hibernate.demo.haa.service;

import jakarta.persistence.EntityManager;
import org.defendev.hibernate.demo.haa.model.FinancialTransaction;
import org.defendev.hibernate.demo.haa.model.GeneralLedgerPosting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;



@Service
public class AddFinancialTransactionPostingService {

    private EntityManager em;

    @Autowired
    public AddFinancialTransactionPostingService(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void execute(Long transactionId, String generalLedgerAccountNumber, String debitOrCredit,
                        BigDecimal amount) {
        final FinancialTransaction transaction = em.find(FinancialTransaction.class, transactionId);

        final GeneralLedgerPosting posting = new GeneralLedgerPosting();
        posting.setFinancialTransaction(transaction);
        posting.setGeneralLedgerAccountNumber(generalLedgerAccountNumber);
        posting.setDebitOrCredit(debitOrCredit);
        posting.setAmount(amount);

        transaction.getGeneralLedgerPostings().add(posting);
    }

}
