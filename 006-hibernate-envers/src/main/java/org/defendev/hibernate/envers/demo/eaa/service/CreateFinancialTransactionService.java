package org.defendev.hibernate.envers.demo.eaa.service;

import jakarta.persistence.EntityManager;
import org.defendev.hibernate.envers.demo.eaa.model.FinancialTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;



@Service
public class CreateFinancialTransactionService {

    private final EntityManager em;

    @Autowired
    public CreateFinancialTransactionService(EntityManager em) {
        this.em = em;
    }

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
