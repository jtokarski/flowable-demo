package org.defendev.flowable.demo.multipoc.config;

import org.defendev.common.time.IClockManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;



@EnableJpaRepositories(
    basePackages = { "org.defendev.flowable.demo.multipoc.repository" },
    entityManagerFactoryRef = "entityManagerFactory",
    transactionManagerRef = "platformTransactionManager"
)
@EnableJpaAuditing(
    dateTimeProviderRef = "zuluDateTimeProvider",
    modifyOnCreate = true
)
@Configuration
public class SpringDataConfig {

    /*
     * Why my implementation of DateTimeProvider returns LocalDateTime among many possible TemporalAccessor(s)?
     * Because I'm following Spring Data default implementation
     *   org.springframework.data.auditing.CurrentDateTimeProvider
     *
     */
    @Bean
    public DateTimeProvider zuluDateTimeProvider(IClockManager clockManager) {
        return () -> Optional.of(clockManager.nowLocalUtc());
    }

}
