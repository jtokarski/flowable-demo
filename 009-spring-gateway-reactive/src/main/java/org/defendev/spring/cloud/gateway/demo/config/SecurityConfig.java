package org.defendev.spring.cloud.gateway.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
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
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

import java.util.Collection;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.union;



@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

    public static final String OAUTH2_REGISTRATION_ID_GOOGLE = "google";

    public static final String OAUTH2_REGISTRATION_ID_AZURE = "azure";

    public static final String OAUTH2_REGISTRATION_ID_GITHUB = "github";

    public static final String OAUTH2_REGISTRATION_ID_SPRING_AUTHZ_SERVER = "sprin6authz";

    /*
     * Whenever setting up a Reactive Gateway like this, ALWAYS anticipate that it will be run
     * on multi-instance and ephemeral containers. Therefore, a random session terminations can alwyas
     * happen and the application have to handle them gracefully. To make it possible,
     * use proper authentication entry point depending on whether you're dealing with XHR request or
     * HTML document request.
     *
     * Key design principle: the .oauth2Login() DSL is intended to be a black box
     * that correctly configures the complex, dynamic behavior required for OAuth2 browser flows
     * (including generating the correct dynamic redirect URI when multiple clients exist). Trying
     * to manually override or extract components from it often breaks that carefully orchestrated logic,
     * and is strongly discouraged.
     *
     * So, if I want to get 401 for XHRs in case of terminated session (instead of 302 to authorization server)
     * the optimal choice is to have separate filter chain that doesn't even configure .oauth2Login().
     *
     * Additionally, remember to assure some SESSION AFFINITY on your ingress/load balancer!
     *
     */
    @Order(0)
    @Bean
    public SecurityWebFilterChain xhrFilterChain(ServerHttpSecurity httpSecurity) {
        final SecurityWebFilterChain webFilterChain = httpSecurity
            .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/confidential-spa/api/**"))
            .authorizeExchange(customizer -> customizer.anyExchange().authenticated())
            .exceptionHandling(customizer -> customizer
                .authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED))
            )
            .build();
        return webFilterChain;
    }

    @Order(1)
    @Bean
    public SecurityWebFilterChain pagesFilterChain(ServerHttpSecurity httpSecurity) {
        final SecurityWebFilterChain webFilterChain = httpSecurity
            .authorizeExchange(customizer -> customizer
                .pathMatchers("/confidential-spa/session-invalidate").permitAll()
                .pathMatchers("/confidential-spa/**").authenticated()
                .anyExchange().permitAll()
            )
            .oauth2Login(Customizer.withDefaults())
            .build();
        return webFilterChain;
    }

    @Bean
    public ReactiveClientRegistrationRepository reactiveClientRegistrationRepository(DefendevGatewayProperties props) {
        final Collection<String> azureRegistrationScopes = union(
            List.of(OidcScopes.OPENID),
            props.getOidc().getAzure().getCustomScopes()
        );
        final ClientRegistration azureRegistration = ClientRegistration
            .withRegistrationId(OAUTH2_REGISTRATION_ID_AZURE)
            .clientName("defendev-gateway at azure")
            .clientId(props.getOidc().getAzure().getClientId())
            .clientSecret(props.getOidc().getAzure().getClientSecret())
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationUri(props.getOidc().getAzure().getAuthorizationUri())
            .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
            .scope(azureRegistrationScopes)
            .tokenUri(props.getOidc().getAzure().getTokenUri())
            .userInfoUri(props.getOidc().getAzure().getUserInfoUri())
            .userNameAttributeName(IdTokenClaimNames.SUB)
            .jwkSetUri(props.getOidc().getAzure().getJwkSetUri())
            .build();

        final ClientRegistration sprin6authzRegistration = ClientRegistration
            .withRegistrationId(OAUTH2_REGISTRATION_ID_SPRING_AUTHZ_SERVER)
            .clientName("defendev-gateway at sprin6authz")
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

        return new InMemoryReactiveClientRegistrationRepository(azureRegistration, sprin6authzRegistration);
    }

}




