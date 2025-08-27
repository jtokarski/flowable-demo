package org.defendev.hibernate.envers.demo.eaa;

import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.defendev.hibernate.H2Util;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

import static org.defendev.hibernate.ScriptPathUtil.createScriptPath;
import static org.hibernate.cfg.JdbcSettings.DIALECT;
import static org.hibernate.cfg.JdbcSettings.FORMAT_SQL;
import static org.hibernate.cfg.JdbcSettings.SHOW_SQL;
import static org.hibernate.cfg.JdbcSettings.USE_SQL_COMMENTS;
import static org.hibernate.cfg.SchemaToolingSettings.HBM2DDL_SCRIPTS_CREATE_APPEND;
import static org.hibernate.cfg.SchemaToolingSettings.JAKARTA_HBM2DDL_CREATE_SCHEMAS;
import static org.hibernate.cfg.SchemaToolingSettings.JAKARTA_HBM2DDL_CREATE_SOURCE;
import static org.hibernate.cfg.SchemaToolingSettings.JAKARTA_HBM2DDL_DATABASE_ACTION;
import static org.hibernate.cfg.SchemaToolingSettings.JAKARTA_HBM2DDL_SCRIPTS_ACTION;
import static org.hibernate.cfg.SchemaToolingSettings.JAKARTA_HBM2DDL_SCRIPTS_CREATE_TARGET;
import static org.hibernate.envers.configuration.EnversSettings.USE_REVISION_ENTITY_WITH_NATIVE_ID;
import static org.hibernate.tool.schema.Action.VALIDATE;
import static org.hibernate.tool.schema.Action.CREATE_ONLY;
import static org.hibernate.tool.schema.Action.NONE;
import static org.hibernate.tool.schema.SourceType.METADATA;



@EnableTransactionManagement
@Configuration
public class EaaScriptJpaConfig {

    private static final Logger log = LogManager.getLogger();

    private static class KeepImports {
        {
            var aa = VALIDATE;
            var ab = NONE;
            var ac = USE_REVISION_ENTITY_WITH_NATIVE_ID;
        }
    }

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

        /*
         * Generally on specifying JPA Properties:
         * Whenever possible, using standardized options instead of Hibernate-specific, e.g.
         *     jakarta.persistence.schema-generation.database.action
         *   instead of
         *     hibernate.hbm2ddl.auto
         *
         * Documentation of all options, see:
         *   https://docs.jboss.org/hibernate/orm/7.0/userguide/html_single/Hibernate_User_Guide.html#settings-schema
         */
        final Properties jpaProperties = new Properties();

        jpaProperties.put(DIALECT, "org.hibernate.dialect.H2Dialect");

        /*
         * Turns out that it's supported to use the combination of:
         *     JAKARTA_HBM2DDL_DATABASE_ACTION : VALIDATE
         *   with
         *     JAKARTA_HBM2DDL_SCRIPTS_ACTION : CREATE_ONLY
         * That is, generate the DDL script to file and validate database schema on start.
         * It doesn't make sense for production but may be useful for development of database schema,
         * when we write migrations manually, but want to be guided by Hibernate-generated DDL.
         *
         */
        jpaProperties.put(JAKARTA_HBM2DDL_CREATE_SCHEMAS, Boolean.FALSE);
        jpaProperties.put(JAKARTA_HBM2DDL_CREATE_SOURCE, METADATA);
        // jpaProperties.put(JAKARTA_HBM2DDL_DATABASE_ACTION, VALIDATE);
        jpaProperties.put(JAKARTA_HBM2DDL_DATABASE_ACTION, NONE);
        jpaProperties.put(JAKARTA_HBM2DDL_SCRIPTS_ACTION, CREATE_ONLY);
        jpaProperties.put(JAKARTA_HBM2DDL_SCRIPTS_CREATE_TARGET, createScriptPath(
            "C:/dev/flowable-demo/006-hibernate-envers/eaa-create-tables-h2.sql"));
        jpaProperties.put(HBM2DDL_SCRIPTS_CREATE_APPEND, Boolean.FALSE);




        jpaProperties.put(SHOW_SQL, Boolean.FALSE);
        jpaProperties.put(FORMAT_SQL, Boolean.FALSE);
        jpaProperties.put(USE_SQL_COMMENTS, Boolean.FALSE);

        emfFactoryBean.setJpaProperties(jpaProperties);
        emfFactoryBean.afterPropertiesSet();
        return emfFactoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
