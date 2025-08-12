package org.defendev.spring.security.oauth2.demo;

import org.springframework.context.ApplicationContext;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;



public class ThymeleafViewResolverBuilder {

    private ApplicationContext applicationContext;

    public ThymeleafViewResolverBuilder() {
    }

    public ThymeleafViewResolverBuilder setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        return this;
    }

    public ThymeleafViewResolver build() {
        final ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(buildTemplateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        viewResolver.setOrder(1);
        viewResolver.setViewNames(new String[] {"*.th"});
        return viewResolver;
    }

    private ITemplateResolver buildTemplateResolver() {
        final SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(this.applicationContext);
        templateResolver.setPrefix("classpath:/view-resources/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(true);
        return templateResolver;
    }

    private SpringTemplateEngine buildTemplateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(buildTemplateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        templateEngine.addDialect(buildSpringSecurityDialect());
        return templateEngine;
    }

    private SpringSecurityDialect buildSpringSecurityDialect() {
        return new SpringSecurityDialect();
    }

}
