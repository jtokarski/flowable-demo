package org.defendev.flowable.demo.multipoc.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flowable.common.engine.impl.history.HistoryLevel;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;



@Configuration
public class FlowableConfig {

    private static final Logger log = LogManager.getLogger();

    @Bean
    public ProcessEngine processEngine(
        ApplicationContext applicationContext,
        @Qualifier("dataSource") DataSource dataSource,
        @Qualifier("platformTransactionManager") PlatformTransactionManager platformTransactionManager)
    {
        final SpringProcessEngineConfiguration springConfig = new SpringProcessEngineConfiguration();
        springConfig.setEngineName("multipocProcEngine");
        springConfig.setApplicationContext(applicationContext);
        springConfig.setDatabaseType("h2");
        springConfig.setDataSource(dataSource);
        springConfig.setTransactionManager(platformTransactionManager);
        springConfig.setCreateDiagramOnDeploy(false);

        /*
         * Keeping Flowable tables in separate schema
         *
         */
        springConfig.setDatabaseTablePrefix("\"amp_bpm\".");
        springConfig.setDatabaseSchema("amp_bpm");
        springConfig.setTablePrefixIsSchema(true);
        springConfig.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE);

        /*
         * For keeping historic values of task-local variables. Especially
         * BpmnDefinitionKey.TASK_VARIABLE_COMPLETION_TYPE
         */
        springConfig.setHistoryLevel(HistoryLevel.FULL);

        final ProcessEngineConfiguration config = springConfig;
        return config.buildProcessEngine();
    }

}
