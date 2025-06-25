package org.defendev.spring.cloud.gateway.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.web.server.SecurityWebFilterChain;



@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

    public static final String CLIENT_NAME = "defendev-gateway";

    public static final String OAUTH2_REGISTRATION_ID_GOOGLE = "google";

    public static final String OAUTH2_REGISTRATION_ID_AZURE = "azure";

    public static final String OAUTH2_REGISTRATION_ID_GITHUB = "github";

    public static final String OAUTH2_REGISTRATION_ID_SPRING_AUTHZ_SERVER = "sprin6authz";

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity
            .authorizeExchange(customizer -> customizer
                .pathMatchers("/confidential-spa/**").authenticated()
                .anyExchange().permitAll()
            )
            .oauth2Login(Customizer.withDefaults())
            .build();
    }

    @Bean
    public ReactiveClientRegistrationRepository reactiveClientRegistrationRepository(DefendevGatewayProperties props) {

        final ClientRegistration sprin6authzRegistration = ClientRegistration
            .withRegistrationId(OAUTH2_REGISTRATION_ID_SPRING_AUTHZ_SERVER)
            .clientName(CLIENT_NAME)
            .clientId(props.getOidc().getSpring().getClientId())
            .clientSecret(props.getOidc().getSpring().getClientSecret())
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
            .authorizationUri(props.getOidc().getSpring().getAuthorizationUri())
            .scope(OidcScopes.OPENID, OidcScopes.PROFILE)
            /*
             * Protocol Endpoint information is available on Authorization Server:
             *   http://localhost:8010/defendev-authz/.well-known/oauth-authorization-server
             *   http://localhost:8010/defendev-authz/.well-known/openid-configuration
             *
             */
            .tokenUri(props.getOidc().getSpring().getTokenUri())
            .userInfoUri(props.getOidc().getSpring().getUserInfoUri())
            .userNameAttributeName(IdTokenClaimNames.SUB)
            .jwkSetUri(props.getOidc().getSpring().getJwkSetUri())
            .build();

        return new InMemoryReactiveClientRegistrationRepository(sprin6authzRegistration);
    }

}




