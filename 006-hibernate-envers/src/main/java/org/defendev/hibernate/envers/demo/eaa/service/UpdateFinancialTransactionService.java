package org.defendev.hibernate.envers.demo.eaa.service;

import jakarta.persistence.EntityManager;
import org.defendev.hibernate.envers.demo.eaa.model.FinancialTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class UpdateFinancialTransactionService {

    private final EntityManager em;

    @Autowired
    public UpdateFinancialTransactionService(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void execute(UpdateFinancialTransactionCommand command) {
        final long transactionId = command.transactionId();
        final FinancialTransaction financialTransaction = em.find(FinancialTransaction.class, transactionId);
        if (command.doUpdateMemo()) {
            financialTransaction.setMemo(command.memo());
        }
        if (command.doUpdateTransactionDateTimeZulu()) {
            financialTransaction.setTransactionDateTimeZulu(command.transactionDateTimeZulu());
        }
    }

}
