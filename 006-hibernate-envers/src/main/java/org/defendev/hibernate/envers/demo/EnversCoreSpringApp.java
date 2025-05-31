package org.defendev.hibernate.envers.demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;



public class EnversCoreSpringApp {

    public static void main(String[] args) {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(EnversCoreSpringConfig.class);
        context.refresh();
        final ApplicationContext applicationContext = context;
    }

}
