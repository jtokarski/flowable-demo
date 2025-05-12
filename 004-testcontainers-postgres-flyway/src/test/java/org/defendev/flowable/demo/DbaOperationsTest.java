package org.defendev.flowable.demo;

import javax.sql.DataSource;
import org.springframework.test.context.ContextConfiguration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;




@ContextConfiguration(classes = {
    TestcontainersDatabaseConfig.class
})
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

}
