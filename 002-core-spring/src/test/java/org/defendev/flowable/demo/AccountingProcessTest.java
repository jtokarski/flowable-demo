package org.defendev.flowable.demo;

import org.defendev.flowable.demo.spring.FlowableCoreSpringConfig;
import org.defendev.flowable.demo.spring.FlowableMultiSpringExtension;
import org.defendev.flowable.demo.spring.ProcessEngineName;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;



@ProcessEngineName(name = "accountingProcessEngine")
@ExtendWith({ SpringExtension.class, FlowableMultiSpringExtension.class })
@ContextConfiguration(classes = { FlowableCoreSpringConfig.class })
public class AccountingProcessTest {

    @Deployment(resources = {"processes/accounting/invoice-posting.bpmn20.xml"})
    @Test
    public void shouldStartInvoicePostingProcess(ProcessEngine accountingProcessEngine,
                                                 RuntimeService runtimeService) {
        // when
        final ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("invoice-posting");
        final String processInstanceId = processInstance.getId();

        // then
        assertThat(accountingProcessEngine.getName()).isEqualTo("accountingProcEngine");
        assertThat(processInstanceId).isNotBlank();
    }

}
