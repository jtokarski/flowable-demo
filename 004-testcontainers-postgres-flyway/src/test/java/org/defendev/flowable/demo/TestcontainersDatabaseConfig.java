package org.defendev.flowable.demo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.testcontainers.containers.GenericContainer;

import javax.sql.DataSource;
import java.util.Map;

import static java.lang.String.format;



@Configuration
public class TestcontainersDatabaseConfig {

    private static final String POSTGRES_PASSWORD = "POSTGRES_PASSWORD";

    @Bean
    public DataSource dbaDataSource(GenericContainer<?> postgresContainer) {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("org.postgresql.Driver");
        final Integer randomPort = postgresContainer.getMappedPort(5432);
        final String host = "192.168.32.3";
        final String jdbcUrl = format("jdbc:postgresql://%s:%d/postgres?currentSchema=defendev_bpm", host, randomPort);
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername("postgres");
        hikariConfig.setPassword("pOST6REs");
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public GenericContainer<?> postgresContainer() {
        final GenericContainer<?> container = new GenericContainer<>("postgres:17.5")
            .withTmpFs(Map.of("/var/lib/postgresql/data", ""))
            .withCommand("-c fsync=off -c full_page_writes=off")
            .withEnv(POSTGRES_PASSWORD, "pOST6REs")
            .withExposedPorts(5432);

        container.start();
        return container;
    }

    @EventListener(ContextStoppedEvent.class)
    public void stopContainers(GenericContainer<?> postgresContainer) {
        postgresContainer.stop();
    }

}
