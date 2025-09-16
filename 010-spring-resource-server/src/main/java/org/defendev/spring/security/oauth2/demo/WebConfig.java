package org.defendev.spring.security.oauth2.demo;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;



@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<ServerDiscriminatorFilter> serverDiscriminatorFilterRegistration() {
        final FilterRegistrationBean<ServerDiscriminatorFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ServerDiscriminatorFilter("010-spring-resource-server"));
        /*
         * Use of Ordered.HIGHEST_PRECEDENCE makes sure that the header is added always,
         * regardless of what Spring Security does (including 401).
         *
         */
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

}
