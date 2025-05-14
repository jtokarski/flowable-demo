package org.defendev.flowable.demo;

import org.flowable.engine.ProcessEngine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.stream.Collectors;

import static java.lang.String.format;



@ContextConfiguration(classes = { TestcontainersDatabaseConfig.class, FlywayConfig.class, FlowableConfig.class })
@ExtendWith(SpringExtension.class)
public class DbaOperationsTest {

    @Test
    public void shouldSelectPostgresVersion(@Autowired DataSource dbaDataSource) {
        final NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dbaDataSource);
        final String queryOutput = jdbcTemplate.query(
            "SELECT version()",
            (ResultSet rs, int rowNum) -> rs.getString(1)).stream().collect(Collectors.joining()
        );
        System.out.println("--- --------------------------------");
        System.out.println("--- Query output: " + queryOutput);
        System.out.println("--- --------------------------------");
    }

    @Test
    public void shouldListPostgresTables(@Autowired DataSource dbaDataSource) {
        final NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dbaDataSource);
        final String queryOutput = jdbcTemplate.query(
            "SELECT * FROM pg_catalog.pg_tables WHERE 'pg_catalog' != schemaname",
            (ResultSet rs, int rowNum) -> {
                final String schemaname = rs.getString("schemaname");
                final String tablename = rs.getString("tablename");
                return format("%s.%s", schemaname, tablename);
            }
        ).stream().collect(Collectors.joining(System.getProperty("line.separator")));
        System.out.println("--- --------------------------------");
        System.out.println("--- Query output:");
        System.out.println(queryOutput);
        System.out.println("--- --------------------------------");
    }

    @Test
    public void processEngine(@Autowired ProcessEngine processEngine) {
        processEngine.getRuntimeService();
    }

}
