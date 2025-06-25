package org.defendev.spring.cloud.gateway.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.DefaultWebSessionManager;
import org.springframework.web.server.session.InMemoryWebSessionStore;
import org.springframework.web.server.session.WebSessionManager;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;



@Configuration
public class WebConfig {

    private static final Logger log = LogManager.getLogger();

    private static final String CLASSPATH_RESOURCES_SOMEAPP = "/view-resources/beta-spa";

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
            .resources((ServerRequest request) -> {
                log.info("RouterFunctions.resources(lookupFunction) - " + request.path());
                if ("/hidden/virtual/document.txt".equals(request.path())) {
                    // i.e. http://localhost:8080/noflo/hidden/virtual/document.txt
                    final Resource hiddenResource = new ClassPathResource("/hidden.txt");
                    return Mono.just(hiddenResource);
                } else {
                    /*
                     * Returning empty Mono doesn't cause any error. Spring will continue with other lookups.
                     */
                    return Mono.empty();
                }
            })
            .resources("/**", dirResource)
            .build();
    }

    @Order(1)
    @Bean
    public RouterFunction<ServerResponse> someAppRouterFunction() {
        final Resource dirResource = new ClassPathResource(CLASSPATH_RESOURCES_SOMEAPP + "/");
        final Resource indexResource = new ClassPathResource(CLASSPATH_RESOURCES_SOMEAPP + "/index.html");
        return RouterFunctions.route()
            .GET("/beta-spa/", accept(MediaType.TEXT_HTML),
                r -> ServerResponse.ok().contentType(MediaType.TEXT_HTML).bodyValue(indexResource)
            )
            .resources("/beta-spa/**", new ClassPathResource(CLASSPATH_RESOURCES_SOMEAPP + "/"))
            .build();
    }

    @Bean
    public ViewResolver thymeleafReactiveViewResolver(ApplicationContext applicationContext) {
        return new ThymeleafViewResolverBuilder()
            .setApplicationContext(applicationContext)
            .build();
    }

    /*
     * Webflux autoconfiguration provides DefaultWebSessionManager and it's perfectly fine.
     * The setup below mostly reproduces the one provided by autoconfiguration.
     * I'm doing it only for demonstration purpose.
     *
     */
    @Bean(name = { WebHttpHandlerBuilder.WEB_SESSION_MANAGER_BEAN_NAME })
    public WebSessionManager webSessionManager() {
        final DefaultWebSessionManager manager = new DefaultWebSessionManager();
        final CookieWebSessionIdResolver sessionIdResolver = new CookieWebSessionIdResolver();
        sessionIdResolver.setCookieName("RSESSION");
        manager.setSessionIdResolver(sessionIdResolver);
        manager.setSessionStore(new InMemoryWebSessionStore());
        return manager;
    }

}
