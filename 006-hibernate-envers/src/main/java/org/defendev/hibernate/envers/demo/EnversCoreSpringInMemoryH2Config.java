package org.defendev.hibernate.envers.demo;

import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static org.hibernate.cfg.JdbcSettings.DIALECT;
import static org.hibernate.cfg.JdbcSettings.FORMAT_SQL;
import static org.hibernate.cfg.JdbcSettings.SHOW_SQL;
import static org.hibernate.cfg.JdbcSettings.USE_SQL_COMMENTS;
import static org.hibernate.cfg.SchemaToolingSettings.JAKARTA_HBM2DDL_CREATE_SCHEMAS;
import static org.hibernate.cfg.SchemaToolingSettings.JAKARTA_HBM2DDL_CREATE_SOURCE;
import static org.hibernate.cfg.SchemaToolingSettings.JAKARTA_HBM2DDL_DATABASE_ACTION;
import static org.hibernate.cfg.SchemaToolingSettings.JAKARTA_HBM2DDL_SCRIPTS_ACTION;
import static org.hibernate.cfg.SchemaToolingSettings.JAKARTA_HBM2DDL_SCRIPTS_CREATE_TARGET;
import static org.hibernate.tool.schema.Action.ACTION_VALIDATE;
import static org.hibernate.tool.schema.Action.CREATE_ONLY;
import static org.hibernate.tool.schema.SourceType.METADATA;



@Profile("inMemoryH2")
@Configuration
public class EnversCoreSpringInMemoryH2Config {

    private static final Logger log = LogManager.getLogger();

    @Bean
    public DataSource dataSource() {
        final JdbcDataSource h2 = new JdbcDataSource();
        h2.setURL("jdbc:h2:mem:accountingBpm;DB_CLOSE_DELAY=-1");
        h2.setUser("sa1");
        h2.setPassword("sa1");
        return h2;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
        final LocalContainerEntityManagerFactoryBean emfFactory = new LocalContainerEntityManagerFactoryBean();
        emfFactory.setPersistenceUnitName("enversCoreSpringPersistenceUnit");
        emfFactory.setPackagesToScan("org.defendev.hibernate.envers.demo.model");
        emfFactory.setDataSource(dataSource);
        emfFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        final Properties jpaProperties = new Properties();
        /*
         * Using standardized options instead of Hibernate-specific whenever possible, e.g.
         *     jakarta.persistence.schema-generation.database.action
         *   instead of
         *     hibernate.hbm2ddl.auto
         *
         * Documentation of all options, see:
         *   https://docs.jboss.org/hibernate/orm/7.0/userguide/html_single/Hibernate_User_Guide.html#settings-schema
         */
        jpaProperties.put(JAKARTA_HBM2DDL_CREATE_SCHEMAS, Boolean.FALSE);
        /*
         * Looks like this is supported to use the combination of:
         *     JAKARTA_HBM2DDL_DATABASE_ACTION : ACTION_VALIDATE
         *   with
         *     JAKARTA_HBM2DDL_SCRIPTS_ACTION : CREATE_ONLY
         * That is, generate the DDL script to file and validate database schema on start.
         * Even if this doesn't make sense for production, may still be useful for this demo.
         *
         */
        jpaProperties.put(JAKARTA_HBM2DDL_CREATE_SOURCE, METADATA);
        jpaProperties.put(JAKARTA_HBM2DDL_DATABASE_ACTION, ACTION_VALIDATE);
        // jpaProperties.put(JAKARTA_HBM2DDL_DATABASE_ACTION, NONE);

        jpaProperties.put(JAKARTA_HBM2DDL_SCRIPTS_ACTION, CREATE_ONLY);
        jpaProperties.put(JAKARTA_HBM2DDL_SCRIPTS_CREATE_TARGET, createScriptPath());
        jpaProperties.put(DIALECT, "org.hibernate.dialect.H2Dialect");
        jpaProperties.put(SHOW_SQL, Boolean.FALSE);
        jpaProperties.put(FORMAT_SQL, Boolean.FALSE);
        jpaProperties.put(USE_SQL_COMMENTS, Boolean.FALSE);
        emfFactory.setJpaProperties(jpaProperties);
        emfFactory.afterPropertiesSet();
        return emfFactory.getObject();
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    private String createScriptPath() {
        final Path path = Path.of("C:/dev/flowable-demo/006-hibernate-envers/create-tables-h2.sql");
        final String pathString = path.toString();
        System.out.println("-----------------------------------------");
        System.out.println("--- Path parent exists: " + Files.exists(path.getParent()));
        System.out.println("--- Writing DDL script to: " + pathString);
        System.out.println("-----------------------------------------");
        return path.toString();
    }

}
