package org.defendev.email.greenmail;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

import java.util.Map;



@ComponentScan(basePackages = { "org.defendev.email.greenmail" }, useDefaultFilters = true)
@EnableAutoConfiguration(exclude = {})
@SpringBootConfiguration
public class GreenmailApp {

    public static void main(String[] args) {
        new SpringApplicationBuilder(GreenmailApp.class)
            .web(WebApplicationType.SERVLET)
            .profiles(new String[] {})
            .properties(Map.of())
            .run(args);
    }

}
