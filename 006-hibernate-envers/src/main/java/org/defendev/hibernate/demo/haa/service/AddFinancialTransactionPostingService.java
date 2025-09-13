package org.defendev.hibernate.demo.haa.service;

import jakarta.persistence.EntityManager;
import org.defendev.hibernate.demo.haa.model.FinancialTransaction;
import org.defendev.hibernate.demo.haa.model.GeneralLedgerPosting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.defendev.common.stream.Streams.stream;



@Service
public class AddFinancialTransactionPostingService {

    public record PostingDto(String generalLedgerAccountNumber, String debitOrCredit, BigDecimal amount) { }

    private EntityManager em;

    @Autowired
    public AddFinancialTransactionPostingService(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void execute(Long transactionId, List<PostingDto> postingDtos) {
        final FinancialTransaction transaction = em.find(FinancialTransaction.class, transactionId);
        stream(postingDtos).forEach(dto -> {
            final GeneralLedgerPosting posting = new GeneralLedgerPosting();
            posting.setFinancialTransaction(transaction);
            posting.setGeneralLedgerAccountNumber(dto.generalLedgerAccountNumber());
            posting.setDebitOrCredit(dto.debitOrCredit());
            posting.setAmount(dto.amount());
            transaction.getGeneralLedgerPostings().add(posting);
        });
    }

}
