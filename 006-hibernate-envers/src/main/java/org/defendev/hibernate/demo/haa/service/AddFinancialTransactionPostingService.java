package org.defendev.hibernate.demo.haa.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.defendev.hibernate.demo.haa.model.FinancialTransaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;



@Service
public class AddFinancialTransactionPostingService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void execute() {

    }

}
