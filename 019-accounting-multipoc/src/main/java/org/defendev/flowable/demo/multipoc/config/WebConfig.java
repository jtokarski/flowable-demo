package org.defendev.flowable.demo.multipoc.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;



@Configuration
public class WebConfig {

    /*
     * This is not primary demo of ServerDiscriminatorFilter!
     * For primary source of truth see subproject 010-spring-resource-server
     *
     */
    @Bean
    public FilterRegistrationBean<ServerDiscriminatorFilter> serverDiscriminatorFilterRegistration() {
        final FilterRegistrationBean<ServerDiscriminatorFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ServerDiscriminatorFilter("019-accounting-multipoc"));
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

}
