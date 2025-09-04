package org.defendev.hibernate.envers.demo.eaa;

import org.defendev.hibernate.envers.demo.eaa.service.AddFinancialTransactionPostingService;
import org.defendev.hibernate.envers.demo.eaa.service.ClearFinancialTransactionPostingsService;
import org.defendev.hibernate.envers.demo.eaa.service.CreateFinancialTransactionService;
import org.defendev.hibernate.envers.demo.eaa.service.UpdateFinancialTransactionCommand;
import org.defendev.hibernate.envers.demo.eaa.service.UpdateFinancialTransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.defendev.hibernate.envers.demo.eaa.model.GeneralLedgerAccount._101_cash;
import static org.defendev.hibernate.envers.demo.eaa.model.GeneralLedgerAccount._128_office_equipment;



@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    EaaRuntimeJpaConfig.class,
    CreateFinancialTransactionService.class,
    AddFinancialTransactionPostingService.class,
    ClearFinancialTransactionPostingsService.class,
    UpdateFinancialTransactionService.class
})
public class EaaRuntimeTest {

    @Test
    public void contextStarts() {
        assertThat(1).isGreaterThan(0);
    }

    @Test
    public void multipleTransactions(
        @Autowired CreateFinancialTransactionService createService,
        @Autowired AddFinancialTransactionPostingService addPostingService,
        @Autowired ClearFinancialTransactionPostingsService clearPostingsService,
        @Autowired UpdateFinancialTransactionService updateService
    ) {
        assertThat(createService).isNotNull();

//        final Long transactionId1 = createService.execute("gumball_watterson3", "Bought chairs and shelves",
//            LocalDateTime.of(2025, Month.AUGUST, 16, 11, 54));
//        addPostingService.execute(transactionId1, _128_office_equipment.name(), "d", new BigDecimal("3000.00"));
//        addPostingService.execute(transactionId1, _101_cash.name(), "c", new BigDecimal("3000.00"));

        final Long transactionId2 = createService.execute("alan_keane7", "Collection on",
            LocalDateTime.of(2025, Month.SEPTEMBER, 3, 21, 23, 03));
        updateService.execute(new UpdateFinancialTransactionCommand(transactionId2, true,
            "Collection on Accounts Receivable",
            false, null));
        updateService.execute(new UpdateFinancialTransactionCommand(transactionId2, true,
            "Collection on Accounts Receivable - One of Bright Consulting’s clients, previously invoiced " +
            "on Jan 12 for services, made a partial payment of $2,500 in cash toward their $4,500 balance. ",
            false, null));

        /*
            Collection on Accounts Receivable
            One of Bright Consulting’s clients, previously invoiced on Jan 12 for services, made a partial payment of $2,500 in cash toward their $4,500 balance. This transaction reduces outstanding receivables, improves cash flow, and shows the importance of active collection efforts to maintain working capital.

            Dr 101 Cash $2,500

            Cr 106 Accounts Receivable $2,500
        */

        assertThat(createService).isNotNull();
    }

}
