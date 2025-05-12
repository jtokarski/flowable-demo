package org.defendev.flowable.demo;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.Location;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;



@Configuration
public class FlywayConfig {

    @Bean
    public Flyway flywaySetup(DataSource dbaDataSource) {
        return Flyway.configure()
            .dataSource(dbaDataSource)
            .schemas("defendev_bpm", "defendev_app")
            .defaultSchema("defendev_app")
            .table("FlywaySchemaHistory")
            .createSchemas(true)
            .locations(new Location("classpath:db/migration/postgres"))
            .sqlMigrationPrefix("V")
            .sqlMigrationSeparator("__")
            .baselineVersion(MigrationVersion.fromVersion("0000"))
            .load();
    }

}
