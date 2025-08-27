package org.defendev.hibernate.envers.demo.eaa;

import org.defendev.hibernate.envers.demo.eaa.service.AddFinancialTransactionPostingService;
import org.defendev.hibernate.envers.demo.eaa.service.ClearFinancialTransactionPostingsService;
import org.defendev.hibernate.envers.demo.eaa.service.CreateFinancialTransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;



@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    EaaRuntimeJpaConfig.class,
    CreateFinancialTransactionService.class,
    AddFinancialTransactionPostingService.class,
    ClearFinancialTransactionPostingsService.class
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
        @Autowired ClearFinancialTransactionPostingsService clearPostingsService
    ) {
        assertThat(createService).isNotNull();
    }

}
