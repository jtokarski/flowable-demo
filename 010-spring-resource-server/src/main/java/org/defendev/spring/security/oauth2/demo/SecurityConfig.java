package org.defendev.spring.security.oauth2.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;



@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // JwtAuthenticationConverter - this is the default one (explicite). Not used but leaving for reference.
        final Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter =
            new JwtAuthenticationConverter();
        final Converter<Jwt, ? extends AbstractAuthenticationToken> figureAuthenticationConverter =
            new FigureAuthenticationConverter();

        final NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder
            .withJwkSetUri("https://login.microsoftonline.com/d9d3ff2d-d7ca-4821-bd09-ed953ac26c13/discovery/v2.0/keys")
            .build();

        http
            .securityMatcher(PathPatternRequestMatcher.withDefaults().matcher("/**"))
            .authorizeHttpRequests(customizer -> customizer.anyRequest().authenticated())
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(customizer -> customizer
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            )
            .oauth2ResourceServer(customizer -> customizer
                .jwt(jwtConfigurer -> jwtConfigurer
                    .decoder(jwtDecoder)
                    .jwtAuthenticationConverter(figureAuthenticationConverter)
                )
            );

        return http.build();
    }


}
