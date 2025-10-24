package org.defendev.spring.cloud.gateway.demo.config;

import org.springframework.context.ApplicationContext;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;
import org.thymeleaf.spring6.ISpringWebFluxTemplateEngine;
import org.thymeleaf.spring6.SpringWebFluxTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.reactive.ThymeleafReactiveViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.nio.charset.StandardCharsets;



public class ThymeleafViewResolverBuilder {

    private ApplicationContext applicationContext;

    public ThymeleafViewResolverBuilder() { }

    public ThymeleafViewResolverBuilder setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        return this;
    }

    public ThymeleafReactiveViewResolver build() {
        final ThymeleafReactiveViewResolver viewResolver = new ThymeleafReactiveViewResolver();
        viewResolver.setTemplateEngine(buildTemplateEngine());
        viewResolver.setDefaultCharset(StandardCharsets.UTF_8);
        viewResolver.setOrder(1);
        viewResolver.setViewNames(new String[] {"*.th"});
        return viewResolver;
    }

    private ISpringWebFluxTemplateEngine buildTemplateEngine() {
		final SpringWebFluxTemplateEngine templateEngine = new SpringWebFluxTemplateEngine();
        templateEngine.setTemplateResolver(buildTemplateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        templateEngine.addDialect(buildSpringSecurityDialect());
        return templateEngine;
    }

    private ITemplateResolver buildTemplateResolver() {
        final SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("classpath:/view-resources/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(true);
        return templateResolver;
    }

    private SpringSecurityDialect buildSpringSecurityDialect() {
        return new SpringSecurityDialect();
    }

}
