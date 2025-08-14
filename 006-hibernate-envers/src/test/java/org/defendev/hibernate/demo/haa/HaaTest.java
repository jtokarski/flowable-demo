package org.defendev.hibernate.demo.haa;

import org.defendev.hibernate.demo.haa.service.AddFinancialTransactionPostingService;
import org.defendev.hibernate.demo.haa.service.ClearFinancialTransactionPostingsService;
import org.defendev.hibernate.demo.haa.service.CreateFinancialTransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
        @Autowired CreateFinancialTransactionService createService
    ) {
        assertThat(createService).isNotNull();
    }

}
