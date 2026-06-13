package org.defendev.restassured.demo.spring;

import org.defendev.restassured.demo.ProfileFlags;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;



@Import({ProfileFlags.class})
@Configuration
public class SpringSetupConfig {

    /**
     * Needed for {@code @Value("${...}")} resolution outside of a full
     * Boot auto-configuration run.
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
