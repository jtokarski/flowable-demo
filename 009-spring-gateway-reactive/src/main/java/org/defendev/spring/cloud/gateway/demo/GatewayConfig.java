package org.defendev.spring.cloud.gateway.demo;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.net.URI;
import java.net.URISyntaxException;

import static org.apache.commons.lang3.StringUtils.substringAfter;



@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) throws URISyntaxException {
        final URI resourcServerUri = new URI("http://localhost:8012/backweb");
        return builder.routes()
            /*
             * For the predicateSpec.path() the base path doesn't have to be specified.
             * However it seems that in previous versions of Spring Cloud Gateway it was necessary.
             *
             */
            .route(predicateSpec -> predicateSpec.path("/confidential-spa/api/deep/**")
                .filters(filterSpec -> filterSpec
                    .tokenRelay()
                    .removeRequestHeader("Cookie")
                    .removeRequestHeader("User-Agent")
                    .filter((exchange, chain) -> {
                        final String resourceServerPath = "/backweb" + substringAfter(
                            exchange.getRequest().getPath().value(),
                            "/webcntx/confidential-spa/api/deep"
                        );
                        final ServerHttpRequest request = exchange.getRequest().mutate()
                            .contextPath("/")
                            .path(resourceServerPath)
                            .build();
                        return chain.filter(exchange.mutate().request(request).build());
                    }))
                .uri(resourcServerUri)
            )
            .build();
    }

}
