package org.defendev.spring.security.oauth2.demo;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

import java.util.Map;



@ComponentScan(basePackages = { "org.defendev.spring.security.oauth2.demo" }, useDefaultFilters = true)
@EnableAutoConfiguration(exclude = {})
@SpringBootConfiguration
public class DefendevResourceServerApp {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DefendevResourceServerApp.class)
            .web(WebApplicationType.SERVLET)
            .profiles(new String[] {})
            .properties(Map.of())
            .run(args);
    }

}
