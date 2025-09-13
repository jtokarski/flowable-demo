package org.defendev.hibernate.demo.haa;

import org.defendev.hibernate.demo.haa.model.FinancialTransaction;
import org.defendev.hibernate.demo.haa.service.AddFinancialTransactionPostingService;
import org.defendev.hibernate.demo.haa.service.AddFinancialTransactionPostingService.PostingDto;
import org.defendev.hibernate.demo.haa.service.ClearFinancialTransactionPostingsService;
import org.defendev.hibernate.demo.haa.service.CreateFinancialTransactionService;
import org.defendev.hibernate.demo.haa.service.FindFinancialTransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;



@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    HaaJpaConfig.class,
    CreateFinancialTransactionService.class,
    AddFinancialTransactionPostingService.class,
    ClearFinancialTransactionPostingsService.class,
    FindFinancialTransactionService.class
})
public class HaaTest {

    @Test
    public void contextStarts() {
    }

    @Test
    public void orphanRemovalInAction(
        @Autowired CreateFinancialTransactionService createService,
        @Autowired AddFinancialTransactionPostingService addPostingService,
        @Autowired ClearFinancialTransactionPostingsService clearPostingsService
    ) throws InterruptedException {
        final Long transactionId = createService.execute("gumball_watterson3", "Bought chairs and shelves",
            LocalDateTime.of(2025, Month.AUGUST, 16, 11, 54));

        addPostingService.execute(transactionId, List.of(new PostingDto("128", "d", new BigDecimal("3000.00"))));
        addPostingService.execute(transactionId, List.of(new PostingDto("101", "c", new BigDecimal("3000.00"))));

        clearPostingsService.execute(transactionId);

        /*
         * This @Test does not contain any assertions because it's meant for making observations
         * of database state in H2 console. Specifically, that after clearPostingsService.execute(),
         * the rows are really removed from GeneralLedgerPosting table.
         *
         */
        assertThat("Add breakpoint here").isNotEqualTo("and look into H2 console.");
    }

    @Test
    public void edgeCaseWithEqualsImplementation(
        @Autowired CreateFinancialTransactionService createService,
        @Autowired AddFinancialTransactionPostingService addPostingService,
        @Autowired FindFinancialTransactionService findService
    ) {
        final Long transactionId = createService.execute("alan_keane7",
            "Consulting Services - financial planning and forecasting service for Ycorpo, " +
            "Partial Cash Payment and Partial Credit",
            LocalDateTime.of(2025, Month.SEPTEMBER, 12, 20, 22, 24));

        addPostingService.execute(transactionId, List.of(new PostingDto("401", "c", new BigDecimal("10500.00"))));

        addPostingService.execute(transactionId, List.of(
            new PostingDto("101", "d", new BigDecimal("5000.00")),
            new PostingDto("106", "d", new BigDecimal("5500.00"))
        ));

        final FinancialTransaction transactionAfter = findService.execute(transactionId);

        /*
         * See comment in org.defendev.hibernate.demo.haa.model.GeneralLedgerPosting.equals()
         */
        assertThat(transactionAfter).isNotNull();
        assertThat(transactionAfter.getGeneralLedgerPostings()).hasSize(3);
    }

}
