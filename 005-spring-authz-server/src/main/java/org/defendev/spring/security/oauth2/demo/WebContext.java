package org.defendev.spring.security.oauth2.demo;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@EnableWebMvc
@Configuration
public class WebContext implements ApplicationContextAware, WebMvcConfigurer {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        final ViewResolver thymeleafViewResolver = new ThymeleafViewResolverBuilder()
            .setApplicationContext(applicationContext)
            .build();
        registry.viewResolver(thymeleafViewResolver);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("/**")
            .addResourceLocations("classpath:/view-resources/")
            .resourceChain(false)
            .addResolver(new SimpleResourceResolver());
    }

}
