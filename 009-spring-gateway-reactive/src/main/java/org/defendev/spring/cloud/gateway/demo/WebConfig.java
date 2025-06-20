package org.defendev.spring.cloud.gateway.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;



@Configuration
public class WebConfig {

    private static final String CLASSPATH_RESOURCES_SOMEAPP = "/view-resources/som-app";

    private static final String CLASSPATH_RESOURCES_MAIN = "/view-resources/main";

    @Order(2)
    @Bean
    public RouterFunction<ServerResponse> mainRouterFunction() {
        final Resource dirResource = new ClassPathResource(CLASSPATH_RESOURCES_MAIN + "/");
        final Resource indexResource = new ClassPathResource(CLASSPATH_RESOURCES_MAIN + "/index.html");
        return RouterFunctions.route()
            .GET("/", accept(MediaType.TEXT_HTML),
                r -> ServerResponse.ok().contentType(MediaType.TEXT_HTML).bodyValue(indexResource)
            )
            .resources("/**", dirResource)
            .build();
    }

    @Order(1)
    @Bean
    public RouterFunction<ServerResponse> someAppRouterFunction() {
        final Resource dirResource = new ClassPathResource(CLASSPATH_RESOURCES_SOMEAPP + "/");
        final Resource indexResource = new ClassPathResource(CLASSPATH_RESOURCES_SOMEAPP + "/index.html");
        return RouterFunctions.route()
            .GET("/sapp/", accept(MediaType.TEXT_HTML),
                r -> ServerResponse.ok().contentType(MediaType.TEXT_HTML).bodyValue(indexResource)
            )
            .resources("/sapp/**", new ClassPathResource(CLASSPATH_RESOURCES_SOMEAPP))
            .build();
    }

}
