package org.defendev.spring.security.oauth2.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;



@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        DefendevAuthnManagerResolver authnManagerResolver
    ) throws Exception {
        http
            .securityMatcher(PathPatternRequestMatcher.withDefaults().matcher("/**"))
            .authorizeHttpRequests(customizer -> customizer.anyRequest().authenticated())
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(customizer -> customizer
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            )
            .oauth2ResourceServer(customizer -> customizer.authenticationManagerResolver(authnManagerResolver));
        return http.build();
    }

}
