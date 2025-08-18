package org.defendev.hibernate.demo.haa;

import org.defendev.hibernate.demo.haa.service.AddFinancialTransactionPostingService;
import org.defendev.hibernate.demo.haa.service.ClearFinancialTransactionPostingsService;
import org.defendev.hibernate.demo.haa.service.CreateFinancialTransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;



@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    HaaJpaConfig.class,
    CreateFinancialTransactionService.class,
    AddFinancialTransactionPostingService.class,
    ClearFinancialTransactionPostingsService.class
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

        addPostingService.execute(transactionId, "1520", "d", new BigDecimal("3000.00"));
        addPostingService.execute(transactionId, "1000", "c", new BigDecimal("3000.00"));

        clearPostingsService.execute(transactionId);
    }

}
