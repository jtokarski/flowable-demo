package org.defendev.flowable.demo.multipoc.config;

import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flowable.common.engine.impl.history.HistoryLevel;
import org.flowable.common.engine.impl.runtime.Clock;
import org.flowable.common.engine.impl.util.DbUtil;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import static java.util.Objects.nonNull;



@Configuration
public class FlowableConfig {

    private static final Logger log = LogManager.getLogger();

    @Bean
    public ProcessEngine processEngine(
        ApplicationContext applicationContext,
        @Qualifier("dataSource") DataSource dataSource,
        @Qualifier("platformTransactionManager") PlatformTransactionManager platformTransactionManager,
        @Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory,
        @Autowired(required = false) Clock flowableClock)
    {
        final SpringProcessEngineConfiguration springConfig = new SpringProcessEngineConfiguration();
        springConfig.setEngineName("multipocProcEngine");
        springConfig.setApplicationContext(applicationContext);
        springConfig.setDatabaseType(DbUtil.DATABASE_TYPE_H2);
        springConfig.setDataSource(dataSource);
        springConfig.setTransactionManager(platformTransactionManager);
        springConfig.setCreateDiagramOnDeploy(false);

        /*
         * Enable JPA entities as process variables (all that is needed)
         *
         */
        springConfig.setJpaEntityManagerFactory(entityManagerFactory);
        springConfig.setJpaHandleTransaction(false);
        springConfig.setJpaCloseEntityManager(false);

        /*
         * I'm only interested in using core BPM execution. For that the IDM and EventRegistry
         * engines are irrelevant (just like DMN engine):
         *   - IDM was extracted as separate module in Flowable 6, manages users/groups/tokens, but for
         *     this purpose I'm relying entirely on Spring
         *   - Event Registry focuses on event driven integration with sources like Kafka, RabbitMQ, JMS
         *
         * But really the primary reason for disabling them was that both:
         *   org.flowable.idm.engine.impl.db.IdmDbSchemaManager.schemaCheckVersion()
         *   org.flowable.eventregistry.impl.db.EventDbSchemaManager
         * were failing to accept the database schema settings:
         *   .setDatabaseSchema()
         *   .setDatabaseTablePrefix()
         *   .setTablePrefixIsSchema()
         * and I didn't want to invest in investigating how to fix it properly:
         *   .setIdmEngineConfigurator(...)
         *   .setEventRegistryConfigurator(...)
         *
         */
        springConfig.setDisableIdmEngine(true);
        springConfig.setDisableEventRegistry(true);

        /*
         * Keeping Flowable tables in separate schema.
         *
         * Depending on RDBMs engine:
         *   - for Postgres and H2 the .setDatabaseSchema("amp_bpm") can be used probably because
         *     those JDBC drivers support java.sql.Connection.setSchema()
         *   - for MS SQL Server the driver doesn't support Connection.setSchema() so I would do:
         *        .setDatabaseTablePrefix("amp_bpm.");
         *        .springConfig.setTablePrefixIsSchema(true);
         *
         */
        springConfig.setDatabaseSchema("amp_bpm");
        springConfig.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE);

        /*
         * For keeping historic values of task-local variables. Especially
         * BpmnDefinitionKey.TASK_VARIABLE_COMPLETION_TYPE
         */
        springConfig.setHistoryLevel(HistoryLevel.FULL);

        if (nonNull(flowableClock)) {
            springConfig.setClock(flowableClock);
        }

        final ProcessEngineConfiguration config = springConfig;
        return config.buildProcessEngine();
    }

    @Lazy(false)
    @Bean
    public Deployment flowableDeployment(ProcessEngine processEngine) {
        final Deployment deployment = processEngine.getRepositoryService().createDeployment()
            .addClasspathResource("processes/madeup-financial-transaction-posting.bpmn20.xml")
            .deploy();
        return deployment;
    }

}
