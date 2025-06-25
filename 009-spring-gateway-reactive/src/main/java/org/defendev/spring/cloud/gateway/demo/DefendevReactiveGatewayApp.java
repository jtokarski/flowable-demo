package org.defendev.spring.cloud.gateway.demo;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import java.util.Map;



@EnableConfigurationProperties({ DefendevGatewayProperties.class })
@ComponentScan(basePackages = {"org.defendev.spring.cloud.gateway.demo"}, useDefaultFilters = true)
@EnableAutoConfiguration(exclude = {ThymeleafAutoConfiguration.class})
@SpringBootConfiguration
public class DefendevReactiveGatewayApp {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DefendevReactiveGatewayApp.class)
            .web(WebApplicationType.REACTIVE)
            .profiles(new String[] {})
            .properties(Map.of())
            .run(args);
    }

}
