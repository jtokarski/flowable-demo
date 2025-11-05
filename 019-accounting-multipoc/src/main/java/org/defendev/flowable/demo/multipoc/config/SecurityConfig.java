package org.defendev.flowable.demo.multipoc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;



@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        final Converter<Jwt, ? extends AbstractAuthenticationToken> figureAuthenticationConverter =
            new FigureAuthenticationConverter();

        final NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder
            .withJwkSetUri("http://localhost:8010/defendev-authz/oauth2/jwks")
            .build();

        http
            .securityMatcher(PathPatternRequestMatcher.withDefaults().matcher("/**"))
            .authorizeHttpRequests(customizer -> customizer
                .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                .anyRequest().authenticated()
            )
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
