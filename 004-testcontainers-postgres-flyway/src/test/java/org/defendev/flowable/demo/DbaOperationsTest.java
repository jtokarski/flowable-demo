package org.defendev.flowable.demo;

import org.flowable.engine.ProcessEngine;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.stream.Collectors;





@ContextConfiguration(classes = { TestcontainersDatabaseConfig.class, FlywayConfig.class, FlowableConfig.class })
@ExtendWith(SpringExtension.class)
public class DbaOperationsTest {

    @Test
    public void testuTest(@Autowired DataSource dbaDataSource) {

        final NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dbaDataSource);
        final String s1 = jdbcTemplate.query("SELECT version()", (ResultSet rs, int rowNum) -> {
            return rs.getString(1);
        }).stream().collect(Collectors.joining());

        System.out.println(s1);
    }

    @Test
    public void processEngine(@Autowired ProcessEngine processEngine) {
        processEngine.getRuntimeService();
    }

}
