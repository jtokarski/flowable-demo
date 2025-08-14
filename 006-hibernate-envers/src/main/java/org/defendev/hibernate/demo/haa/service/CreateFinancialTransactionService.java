package org.defendev.hibernate.demo.haa.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.defendev.hibernate.demo.haa.model.FinancialTransaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;



@Service
public class CreateFinancialTransactionService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Long execute(String recordedBy, String memo, LocalDateTime transactionDateTimeZulu) {
        final FinancialTransaction financialTransaction = new FinancialTransaction();
        financialTransaction.setRecordedBy(recordedBy);
        financialTransaction.setMemo(memo);
        financialTransaction.setTransactionDateTimeZulu(transactionDateTimeZulu);

        em.persist(financialTransaction);
        em.flush();

        return financialTransaction.getId();
    }

}
