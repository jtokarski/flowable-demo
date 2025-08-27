package org.defendev.hibernate.envers.demo.eaa.service;

import jakarta.persistence.EntityManager;
import org.defendev.hibernate.envers.demo.eaa.model.FinancialTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class ClearFinancialTransactionPostingsService {

    private EntityManager em;

    @Autowired
    public ClearFinancialTransactionPostingsService(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void execute(Long transactionId) {
        final FinancialTransaction transaction = em.find(FinancialTransaction.class, transactionId);
        transaction.getGeneralLedgerPostings().forEach(p -> p.setFinancialTransaction(null));
        transaction.getGeneralLedgerPostings().clear();
    }

}
