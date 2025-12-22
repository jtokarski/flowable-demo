package org.defendev.flowable.demo.multipoc.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.Server;
import org.hibernate.envers.configuration.EnversSettings;
import org.hibernate.envers.strategy.internal.ValidityAuditStrategy;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

import static org.defendev.common.io.PathUtil.outputFilePath;
import static org.hibernate.cfg.JdbcSettings.DIALECT;
import static org.hibernate.cfg.JdbcSettings.FORMAT_SQL;
import static org.hibernate.cfg.JdbcSettings.SHOW_SQL;
import static org.hibernate.cfg.JdbcSettings.USE_SQL_COMMENTS;
import static org.hibernate.cfg.ManagedBeanSettings.BEAN_CONTAINER;
import static org.hibernate.cfg.MappingSettings.GLOBALLY_QUOTED_IDENTIFIERS;
import static org.hibernate.cfg.SchemaToolingSettings.HBM2DDL_SCRIPTS_CREATE_APPEND;
import static org.hibernate.cfg.SchemaToolingSettings.JAKARTA_HBM2DDL_CREATE_SCHEMAS;
import static org.hibernate.cfg.SchemaToolingSettings.JAKARTA_HBM2DDL_CREATE_SOURCE;
import static org.hibernate.cfg.SchemaToolingSettings.JAKARTA_HBM2DDL_DATABASE_ACTION;
import static org.hibernate.cfg.SchemaToolingSettings.JAKARTA_HBM2DDL_SCRIPTS_ACTION;
import static org.hibernate.cfg.SchemaToolingSettings.JAKARTA_HBM2DDL_SCRIPTS_CREATE_TARGET;
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
public class JpaConfig {

    private static final Logger log = LogManager.getLogger();

    @Bean
    public DataSource dataSource() {
        final JdbcDataSource h2 = new JdbcDataSource();
        h2.setURL("jdbc:h2:mem:multipoc;DB_CLOSE_DELAY=-1");
        h2.setUser("sa");
        h2.setPassword("sa");
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("create-schemas-h2.sql"));
        populator.execute(h2);
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
    public EntityManagerFactory entityManagerFactory(
        DataSource dataSource,
        ConfigurableListableBeanFactory configurableListableBeanFactory
    ) {
        final LocalContainerEntityManagerFactoryBean emfFactoryBean = new LocalContainerEntityManagerFactoryBean();
        emfFactoryBean.setPersistenceUnitName("multipocPersistenceUnit");
        emfFactoryBean.setDataSource(dataSource);
        emfFactoryBean.setPackagesToScan("org.defendev.flowable.demo.multipoc.model");
        final HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        emfFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

        final Properties jpaProperties = new Properties();
        jpaProperties.put(DIALECT, "org.hibernate.dialect.H2Dialect");
        jpaProperties.put(GLOBALLY_QUOTED_IDENTIFIERS, Boolean.TRUE);
        jpaProperties.put(JAKARTA_HBM2DDL_CREATE_SCHEMAS, Boolean.FALSE);
        jpaProperties.put(JAKARTA_HBM2DDL_CREATE_SOURCE, METADATA);
        jpaProperties.put(JAKARTA_HBM2DDL_DATABASE_ACTION, CREATE_ONLY);

        /*
         * Necessary to enable bean injection into o.d.f.d.m.c.envers.DefendevAccountingRevisionListener.
         * Same as in org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaConfiguration
         */
        jpaProperties.put(BEAN_CONTAINER, new SpringBeanContainer(configurableListableBeanFactory));

        jpaProperties.put(AUTO_REGISTER, Boolean.TRUE);
        jpaProperties.put(REVISION_FIELD_NAME, "idOfEnversRevision");
        jpaProperties.put(REVISION_TYPE_FIELD_NAME, "enversRevisionType");
        jpaProperties.put(MODIFIED_COLUMN_NAMING_STRATEGY, "improved");
        jpaProperties.put(AUDIT_TABLE_SUFFIX, "_Audit");
        jpaProperties.put(MODIFIED_FLAG_SUFFIX, "_Modified");
        jpaProperties.put(AUDIT_STRATEGY, ValidityAuditStrategy.class.getName());
        jpaProperties.put(AUDIT_STRATEGY_VALIDITY_END_REV_FIELD_NAME, "idOfEndEnversRevision");

        jpaProperties.put(EnversSettings.STORE_DATA_AT_DELETE, Boolean.TRUE);

        jpaProperties.put(JAKARTA_HBM2DDL_SCRIPTS_ACTION, CREATE_ONLY);
        jpaProperties.put(JAKARTA_HBM2DDL_SCRIPTS_CREATE_TARGET, outputFilePath(
            "C:/dev/flowable-demo/019-accounting-multipoc/multipoc-create-tables-h2.sql"));
        jpaProperties.put(HBM2DDL_SCRIPTS_CREATE_APPEND, Boolean.FALSE);

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
