package org.defendev.flowable.demo.spring;

import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.repository.Deployment;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;



public class FlowableCoreSpringApp {

    public static void main(String[] args) {
        /*
         * The below is a demonstration of starting Spring Context from main() method.
         * This it to highlight and emphasize that we are able to work with
         * pure-Spring setup, without any use of Spring Boot.
         *
         */
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(FlowableCoreSpringConfig.class);
        context.refresh();

        final ApplicationContext applicationContext = context;


        //   --- HR Process Engine ---
        final ProcessEngine hrProcessEngine = applicationContext.getBean("hrProcessEngine",
            ProcessEngine.class);
        assertThat(hrProcessEngine).isNotNull();

        final Deployment hrDeployment = hrProcessEngine.getRepositoryService().createDeployment()
            .addClasspathResource("processes/hr/go-out.bpmn20.xml")
            .addClasspathResource("processes/hr/absence-approval.bpmn20.xml")
            .addClasspathResource("decisions/hr/dmn-meanOfTransportSelection.dmn")
            .deploy();


        //   --- Accounting Process Engine ---
        final ProcessEngine accountingProcessEngine = applicationContext.getBean("accountingProcessEngine",
            ProcessEngine.class);
        assertThat(accountingProcessEngine).isNotNull();

        final Deployment accountingDeployment = accountingProcessEngine.getRepositoryService().createDeployment()
            .addClasspathResource("processes/accounting/invoice-posting.bpmn20.xml")
            .deploy();


        //   --- prove separation of Process Engines ---
        assertThatExceptionOfType(FlowableException.class).isThrownBy(() -> {
            hrProcessEngine.getRuntimeService().startProcessInstanceByKey("invoice-posting");
        });
        assertThatNoException().isThrownBy(() -> {
            hrProcessEngine.getRuntimeService().startProcessInstanceByKey("go-out", Map.of(
                "distanceKilometers", 29,
                "celsiusDegreesOutside", 42
            ));
        });
    }

}
