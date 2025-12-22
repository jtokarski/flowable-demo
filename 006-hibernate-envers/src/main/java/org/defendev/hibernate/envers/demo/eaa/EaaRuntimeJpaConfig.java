package org.defendev.hibernate.envers.demo.eaa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.defendev.hibernate.H2Util;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.Server;
import org.hibernate.envers.configuration.EnversSettings;
import org.hibernate.envers.strategy.internal.ValidityAuditStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

import static org.hibernate.cfg.JdbcSettings.DIALECT;
import static org.hibernate.cfg.JdbcSettings.FORMAT_SQL;
import static org.hibernate.cfg.JdbcSettings.SHOW_SQL;
import static org.hibernate.cfg.JdbcSettings.USE_SQL_COMMENTS;
import static org.hibernate.cfg.SchemaToolingSettings.JAKARTA_HBM2DDL_CREATE_SCHEMAS;
import static org.hibernate.cfg.SchemaToolingSettings.JAKARTA_HBM2DDL_CREATE_SOURCE;
import static org.hibernate.cfg.SchemaToolingSettings.JAKARTA_HBM2DDL_DATABASE_ACTION;
import static org.hibernate.envers.boot.internal.EnversIntegrator.AUTO_REGISTER;
import static org.hibernate.envers.configuration.EnversSettings.AUDIT_STRATEGY;
import static org.hibernate.envers.configuration.EnversSettings.AUDIT_STRATEGY_VALIDITY_END_REV_FIELD_NAME;
import static org.hibernate.envers.configuration.EnversSettings.AUDIT_TABLE_SUFFIX;
import static org.hibernate.envers.configuration.EnversSettings.MODIFIED_COLUMN_NAMING_STRATEGY;
import static org.hibernate.envers.configuration.EnversSettings.MODIFIED_FLAG_SUFFIX;
import static org.hibernate.envers.configuration.EnversSettings.REVISION_FIELD_NAME;
import static org.hibernate.envers.configuration.EnversSettings.REVISION_TYPE_FIELD_NAME;
import static org.hibernate.tool.schema.Action.CREATE_ONLY;
import static org.hibernate.tool.schema.SourceType.METADATA;



@EnableTransactionManagement
@Configuration
public class EaaRuntimeJpaConfig {

    private static final Logger log = LogManager.getLogger();

    @Bean
    public DataSource dataSource() {
        final JdbcDataSource h2 = new JdbcDataSource();
        h2.setURL("jdbc:h2:mem:haa;DB_CLOSE_DELAY=-1");
        h2.setUser("haa");
        h2.setPassword("haa");
        return h2;
    }

    @Lazy(false)
    @Bean
    public Server h2TcpServer(DataSource dataSource) throws SQLException {
        return H2Util.createH2TcpServer();
    }

    @Lazy(false)
    @Bean
    public Server h2WebServer(DataSource dataSource) throws SQLException {
        return H2Util.createH2WebServer();
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
        final LocalContainerEntityManagerFactoryBean emfFactoryBean = new LocalContainerEntityManagerFactoryBean();
        emfFactoryBean.setPersistenceUnitName("eaaPersistenceUnit");
        emfFactoryBean.setDataSource(dataSource);
        emfFactoryBean.setPackagesToScan("org.defendev.hibernate.envers.demo.eaa");
        final HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        emfFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

        final Properties jpaProperties = new Properties();
        jpaProperties.put(DIALECT, "org.hibernate.dialect.H2Dialect");
        jpaProperties.put(JAKARTA_HBM2DDL_CREATE_SCHEMAS, Boolean.FALSE);
        jpaProperties.put(JAKARTA_HBM2DDL_CREATE_SOURCE, METADATA);
        jpaProperties.put(JAKARTA_HBM2DDL_DATABASE_ACTION, CREATE_ONLY);

        jpaProperties.put(REVISION_FIELD_NAME, "idOfVersioningRevision");
        jpaProperties.put(REVISION_TYPE_FIELD_NAME, "versioningRevisionType");
        jpaProperties.put(MODIFIED_COLUMN_NAMING_STRATEGY, "improved");
        jpaProperties.put(AUDIT_TABLE_SUFFIX, "_Audit");
        jpaProperties.put(MODIFIED_FLAG_SUFFIX, "_Modified");
        jpaProperties.put(AUDIT_STRATEGY, ValidityAuditStrategy.class.getName());
        jpaProperties.put(AUDIT_STRATEGY_VALIDITY_END_REV_FIELD_NAME, "idOfEndVersioningRevision");

        /*
         * When using custom Envers listeners to implement Conditional Auditing, this auto-register have
         * to be disabled. Otherwise the application will fail to start shouting about duplicate listeners.
         *
         */
        jpaProperties.put(AUTO_REGISTER, Boolean.FALSE);

        /*
         *
         *
         *
         * With Conditional Auditing (skipping of revisions) I ASSUME (although not empirically proven) that
         * I rather need to store data at delete revision. This is because at the moment of delete we can be ahead
         * of some skipped revisions and would better store the last state.
         * Advantage of this is that I can have NOT NULL constraints on my audit tables.
         *
         */
        jpaProperties.put(EnversSettings.STORE_DATA_AT_DELETE, Boolean.TRUE);

        /*
         * The JdbcSettings.SHOW_SQL = true would cause Hibernate to log directly to console, bypassing
         * the logging framework. It's better when replaced with org.hibernate.SQL logger set to 'debug'
         *
         */
        jpaProperties.put(SHOW_SQL, Boolean.FALSE);
        jpaProperties.put(FORMAT_SQL, Boolean.TRUE);
        jpaProperties.put(USE_SQL_COMMENTS, Boolean.FALSE);

        emfFactoryBean.setJpaProperties(jpaProperties);
        emfFactoryBean.afterPropertiesSet();
        return emfFactoryBean.getObject();
    }

    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return SharedEntityManagerCreator.createSharedEntityManager(entityManagerFactory);
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
