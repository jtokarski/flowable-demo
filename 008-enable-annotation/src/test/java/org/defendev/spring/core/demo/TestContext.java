package org.defendev.spring.core.demo;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;



@EnableDataSourceChecker(dataSourceRef = "s4lesDataSource")
@Configuration
public class TestContext {

    @Bean
    public DataSource acc0untingDataSource() {
        final JdbcDataSource h2 = new JdbcDataSource();
        h2.setURL("jdbc:h2:mem:acc0untingDb;DB_CLOSE_DELAY=-1");
        h2.setUser("sa");
        h2.setPassword("sa");
       return h2;
    }

    @Bean
    public DataSource s4lesDataSource() {
        final JdbcDataSource h2 = new JdbcDataSource();
        h2.setURL("jdbc:h2:mem:s4lesDb;DB_CLOSE_DELAY=-1");
        h2.setUser("sa");
        h2.setPassword("sa");
        return h2;
    }

}
