package org.defendev.flowable.demo;

import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;



public class TestEngines {

    public static ProcessEngineConfiguration setupProcessEngineConfiguration() {
        final ProcessEngineConfiguration config = new StandaloneProcessEngineConfiguration()
            .setEngineName("empty0")
            .setJdbcUrl("jdbc:h2:mem:flowableTest;DB_CLOSE_DELAY=-1")
            .setJdbcUsername("sa1")
            .setJdbcPassword("sa1")
            .setJdbcDriver("org.h2.Driver")
            .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        return config;
    }

}
