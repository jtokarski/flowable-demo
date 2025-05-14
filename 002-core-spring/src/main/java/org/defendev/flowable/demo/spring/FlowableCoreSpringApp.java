package org.defendev.flowable.demo.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;



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

        // What else I would like to demonstrate here?
        //   -> a proof that process and dmn engines are indeed in application context
        //   -> I can use repository services to deploy some processess, decisions
        //   -> I can start/complete process/decision

    }

}
