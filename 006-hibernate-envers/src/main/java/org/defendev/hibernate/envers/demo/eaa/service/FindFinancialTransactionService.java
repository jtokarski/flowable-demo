package org.defendev.hibernate.envers.demo.eaa.service;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import org.defendev.hibernate.envers.demo.eaa.model.FinancialTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class FindFinancialTransactionService {

    private final EntityManager em;

    @Autowired
    public FindFinancialTransactionService(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public FinancialTransaction execute(Long transactionId) {
        final EntityGraph<FinancialTransaction> entityGraph = em.createEntityGraph(FinancialTransaction.class);
        entityGraph.addAttributeNode("generalLedgerPostings");
        final FinancialTransaction financialTransaction = em.find(entityGraph, transactionId);
        return financialTransaction;
    }

}
