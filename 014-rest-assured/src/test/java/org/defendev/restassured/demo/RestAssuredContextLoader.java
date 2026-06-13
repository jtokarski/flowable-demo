package org.defendev.restassured.demo;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.SmartContextLoader;



public class RestAssuredContextLoader implements SmartContextLoader {

    /*
     * modern (from SmartContextLoader which takes precedence over AbstractContextLoader)
     */
    @Override
    public ApplicationContext loadContext(MergedContextConfiguration mergedConfig) throws Exception {
        final String[] activeProfiles = mergedConfig.getActiveProfiles();
        final ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(mergedConfig.getClasses())
            .web(WebApplicationType.NONE)
            .profiles(activeProfiles)
            .run();
        return applicationContext;
    }

    /*
     * modern (from SmartContextLoader which takes precedence over AbstractContextLoader)
     */
    @Override
    public void processContextConfiguration(ContextConfigurationAttributes configAttributes) {
        // do nothing intentionally
    }

}
