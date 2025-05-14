package org.defendev.flowable.demo.spring;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flowable.dmn.engine.DmnEngine;
import org.flowable.dmn.engine.DmnEngineConfiguration;
import org.flowable.dmn.engine.configurator.DmnEngineConfigurator;
import org.flowable.dmn.spring.SpringDmnEngineConfiguration;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;



@Configuration
public class FlowableCoreSpringConfig {

    private static final Logger log = LogManager.getLogger();

    @Bean
    public DataSource accountingBpmDataSource() {
        final JdbcDataSource h2 = new JdbcDataSource();
        h2.setURL("jdbc:h2:mem:accountingBpm;DB_CLOSE_DELAY=-1");
        h2.setUser("sa1");
        h2.setPassword("sa1");
        return h2;
    }

    @Bean
    public DataSource hrBpmDataSource() {
        final JdbcDataSource h2 = new JdbcDataSource();
        h2.setURL("jdbc:h2:mem:hrBpm;DB_CLOSE_DELAY=-1");
        h2.setUser("sa2");
        h2.setPassword("sa2");
        return h2;
    }

    @Bean
    public Server h2TcpServer(
        @Qualifier("accountingBpmDataSource") DataSource accountingBpmDataSource,
        @Qualifier("hrBpmDataSource") DataSource hrBpmDataSource
    ) throws SQLException {
        Validate.validState(accountingBpmDataSource instanceof JdbcDataSource);
        Validate.validState(hrBpmDataSource instanceof JdbcDataSource);
        final String[] h2Args = new String[] {
            "-trace",
            "-tcpPort", "9092",
            "-tcpAllowOthers",
            "-ifExists"
        };
        final Server h2Server = Server.createTcpServer(h2Args);
        h2Server.start();
        return h2Server;
    }

    @Bean
    public PlatformTransactionManager accountingBpmTransactionManager(
        @Qualifier("accountingBpmDataSource") DataSource accountingBpmDataSource
    ) {
        return new JdbcTransactionManager(accountingBpmDataSource);
    }

    @Bean
    public PlatformTransactionManager hrBpmTransactionManager(
        @Qualifier("hrBpmDataSource") DataSource hrBpmDataSource
    ) {
        return new JdbcTransactionManager(hrBpmDataSource);
    }

    @Bean
    public ProcessEngine accountingProcessEngine(
        ApplicationContext applicationContext,
        @Qualifier("accountingBpmDataSource") DataSource accountingBpmDataSource,
        @Qualifier("accountingBpmTransactionManager") PlatformTransactionManager accountingBpmTransactionManager
    ) {
        Validate.notNull(applicationContext);
        /*
         * I don't think the SpringProcessEngineConfiguration ever needs to be exposed as Spring Bean.
         * In tutorial (https://www.flowable.com/open-source/docs/bpmn/ch05-Spring)
         * it's probably exposed because there are no means to avoid this in XML configuration,
         * like this is easily done in @Bean-annotated methods.
         *
         */
        final SpringProcessEngineConfiguration springConfig = new SpringProcessEngineConfiguration();
        springConfig.setEngineName("accountingProcEngine");
        springConfig.setApplicationContext(applicationContext);
        springConfig.setDatabaseType("h2");
        springConfig.setDataSource(accountingBpmDataSource);
        springConfig.setTransactionManager(accountingBpmTransactionManager);
        springConfig.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE);
        final ProcessEngineConfiguration config = springConfig;
        return config.buildProcessEngine();
    }

    @Bean
    public DmnEngineConfiguration hrSpringDmnEngineConfiguration(
        ApplicationContext applicationContext,
        @Qualifier("hrBpmDataSource") DataSource hrBpmDataSource,
        @Qualifier("hrBpmTransactionManager") PlatformTransactionManager hrBpmTransactionManager
    ) {
        final SpringDmnEngineConfiguration springConfig = new SpringDmnEngineConfiguration();
        springConfig.setEngineName("hrDmnEngine");
        springConfig.setApplicationContext(applicationContext);
        springConfig.setDatabaseType("h2");
        springConfig.setDataSource(hrBpmDataSource);
        springConfig.setTransactionManager(hrBpmTransactionManager);
        springConfig.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE);
        return springConfig;
    }

    @Bean
    public DmnEngine hrDmnEngine(DmnEngineConfiguration config) {
        return config.buildDmnEngine();
    }

    @Bean
    public ProcessEngine hrProcessEngine(
        ApplicationContext applicationContext,
        @Qualifier("hrBpmDataSource") DataSource hrBpmDataSource,
        @Qualifier("hrBpmTransactionManager") PlatformTransactionManager hrBpmTransactionManager,
        DmnEngineConfiguration hrSpringDmnEngineConfiguration
    ) {
        Validate.notNull(applicationContext);
        final SpringProcessEngineConfiguration springConfig = new SpringProcessEngineConfiguration();
        springConfig.setEngineName("hrProcEngine");
        springConfig.setApplicationContext(applicationContext);
        springConfig.setDatabaseType("h2");
        springConfig.setDataSource(hrBpmDataSource);
        springConfig.setTransactionManager(hrBpmTransactionManager);
        springConfig.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE);
        final DmnEngineConfigurator hrDmnConfigurator = new DmnEngineConfigurator()
            .setDmnEngineConfiguration(hrSpringDmnEngineConfiguration);
        springConfig.addConfigurator(hrDmnConfigurator);
        final ProcessEngineConfiguration config = springConfig;
        return config.buildProcessEngine();
    }

}
