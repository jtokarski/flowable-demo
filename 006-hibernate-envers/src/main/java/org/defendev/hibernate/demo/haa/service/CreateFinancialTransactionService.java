package org.defendev.hibernate.demo.haa.service;

import jakarta.persistence.EntityManager;
import org.defendev.hibernate.demo.haa.model.FinancialTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;



@Service
public class CreateFinancialTransactionService {

    /*
     * Why not using @PersistenceContext ?
     * See:
     *   https://docs.spring.io/spring-framework/reference/data-access/orm/jpa.html#orm-jpa-dao-autowired
     *
     * Wonder if Spring Data also internally relies on either of:
     *   - org.springframework.orm.jpa.SharedEntityManagerCreator
     *   - org.springframework.orm.jpa.support.SharedEntityManagerBean
     *
     */
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
