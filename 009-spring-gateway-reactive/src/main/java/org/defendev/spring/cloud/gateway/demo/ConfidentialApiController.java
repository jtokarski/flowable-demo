package org.defendev.spring.cloud.gateway.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.server.AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.bind.annotation.RequestMethod.GET;



@RequestMapping(path = { "/confidential-spa/api" })
@Controller
public class ConfidentialApiController {

    @Autowired
    public ConfidentialApiController(ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
        this.authorizedClientRepository = authorizedClientRepository;
    }

    private static class KeepImports {
        AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository a;
        InMemoryOAuth2AuthorizedClientService b;
    }

    /*
     *
     * The ServerOAuth2AuthorizedClientRepository is used internally by Spring Cloud Gateway .tokenRelay().
     *
     * Expected to be of concrete class AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository
     * at runtime. That means it will relay on [principal: client] map rather than storing client
     * as session attribute.
     *
     * And AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository delegates to implementation of
     * ReactiveOAuth2AuthorizedClientService (expecting InMemoryOAuth2AuthorizedClientService)
     *
     *
     */
    private final ServerOAuth2AuthorizedClientRepository authorizedClientRepository;

    @RequestMapping(method = GET, path = { "shallow" })
    public Mono<ResponseEntity<ShallowDto>> getShallowInfo() {
        return ReactiveSecurityContextHolder.getContext()
            .map( securityContext -> {
                final int randomInt = ThreadLocalRandom.current().nextInt(1_000, 10_000);
                final Authentication authentication = securityContext.getAuthentication();
                assertThat(authentication)
                    .as("authentication expected to be instance of OAuth2AuthenticationToken")
                    .isInstanceOf(OAuth2AuthenticationToken.class);
                final Object principal = authentication.getPrincipal();
                assertThat(principal)
                    .as("principal expected to be instance of DefaultOAuth2User")
                    .isInstanceOf(DefaultOAuth2User.class);
                final String shallowKnowledge = format(
                    "The ranodm int is %d. The authentication is %s, and the principal is %s",
                    randomInt, authentication.getClass().getCanonicalName(), principal.getClass().getCanonicalName());
                final ShallowDto dto = new ShallowDto(shallowKnowledge);
                return ResponseEntity.ok(dto);
            } );
    }

    /*
     * Note that the Access Token returned by this endpoint may likely not be the exact one that was obtained
     * during exchange of Auth Code - it may be another one obtained using Refresh Token.
     *
     */
    @RequestMapping(method = GET, path = { "curio" })
    public Mono<ResponseEntity<CurioDto>> getCurio(@CurrentSecurityContext SecurityContext securityContext,
                                                   ServerWebExchange serverWebExchange) {
        assertThat(authorizedClientRepository)
            .isInstanceOf(AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository.class);
        /*
         * Authentication is needed to determine the Client Registration Id
         */
        final Authentication authentication = securityContext.getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken oauth2Authentication) {
            final String clientRegistrationId = oauth2Authentication.getAuthorizedClientRegistrationId();
            final Mono<OAuth2AuthorizedClient> clientMono = authorizedClientRepository.loadAuthorizedClient(
                clientRegistrationId, authentication, serverWebExchange);
            return clientMono.map(oAuth2AuthorizedClient -> {
                final String accessTokenValue = oAuth2AuthorizedClient.getAccessToken().getTokenValue();
                final CurioDto dto = new CurioDto(accessTokenValue);
                return ResponseEntity.ok(dto);
            });
        } else {
            throw new IllegalStateException("Authentication exptected to be OAuth2AuthenticationToken");
        }
    }

}
