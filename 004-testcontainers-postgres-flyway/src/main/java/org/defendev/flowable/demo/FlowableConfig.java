package org.defendev.flowable.demo;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flywaydb.core.Flyway;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;



@Configuration
public class FlowableConfig {

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource, Flyway flyway) {

        // yes, I know this is not the right place for doing this:
        flyway.baseline();
        flyway.migrate();


        return new JdbcTransactionManager(dataSource);
    }

    @Bean
    public ProcessEngine processEngine(
        ApplicationContext applicationContext,
        DataSource dataSource,
        PlatformTransactionManager transactionManager
    ) {
        final SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
        config.setEngineName("processEngine");
        config.setApplicationContext(applicationContext);
        config.setDataSource(dataSource);
        config.setDatabaseType("postgres");
        config.setTransactionManager(transactionManager);
        config.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE);
        config.setDatabaseTablePrefix("defendev_bpm.");
        config.setTablePrefixIsSchema(true);
        return config.buildEngine();
    }

}
