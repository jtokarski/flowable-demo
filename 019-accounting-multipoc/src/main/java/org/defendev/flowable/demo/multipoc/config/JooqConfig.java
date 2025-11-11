package org.defendev.flowable.demo.multipoc.config;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;



@Configuration
public class JooqConfig {

    @Bean
    public DataSourceConnectionProvider jooqConnectionProvider(DataSource dataSource) {
        return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
    }

    @Bean
    public DefaultConfiguration jooqConfiguration(DataSourceConnectionProvider jooqConnectionProvider) {
        final DefaultConfiguration config = new DefaultConfiguration();
        config.set(jooqConnectionProvider);
        config.set(SQLDialect.H2);

        /*
         * https://www.jooq.org/doc/3.20/manual/sql-building/dsl-context/custom-settings/settings-execute-logging/
         * https://www.jooq.org/doc/3.20/manual/sql-execution/logging/
         *
         * Consider custom abbreviation of bind values:
         * https://www.jooq.org/doc/3.20/manual/sql-building/queryparts/custom-sql-transformation/transformation-bind-value-abbreviation/
         */
        final Settings settings = new Settings().withExecuteLogging(true);
        config.set(settings);

        return config;
    }

    @Bean
    public DSLContext jooqDslContext(DefaultConfiguration jooqConfiguration) {
        return new DefaultDSLContext(jooqConfiguration);
    }

}
