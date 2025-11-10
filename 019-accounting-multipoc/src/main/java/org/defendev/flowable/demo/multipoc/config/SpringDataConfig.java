package org.defendev.flowable.demo.multipoc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



@EnableJpaRepositories(
    basePackages = { "org.defendev.flowable.demo.multipoc.repository" },
    entityManagerFactoryRef = "entityManagerFactory",
    transactionManagerRef = "platformTransactionManager"
)
@Configuration
public class SpringDataConfig {
}
