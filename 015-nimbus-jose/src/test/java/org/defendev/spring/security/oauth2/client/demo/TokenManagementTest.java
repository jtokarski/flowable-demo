package org.defendev.spring.security.oauth2.client.demo;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.time.Duration;
import java.time.Instant;
import java.util.List;



public class TokenManagementTest {

    /*
     * This relies on 005-spring-authz-server running.
     */
    @Test
    public void shoulWorkCorrectlyAndNotIncorrectly() throws InterruptedException {

        //
        final ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("53d14824")
            .clientId("950f60")
            .clientSecret("034a75a60fa9e34c17bb4c3521")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .tokenUri("http://localhost:8010/defendev-authz/oauth2/token")
            .scope()
            .build();
        final ClientRegistrationRepository clientRegistrationRepository =
            new InMemoryClientRegistrationRepository(List.of(clientRegistration));

        //
        final OAuth2AuthorizedClientService authorizedClientService =
            new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);

        //
        final OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
            .clientCredentials()
            .build();
        final AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
            new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                clientRegistrationRepository,
                authorizedClientService
            );
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        /*
         *
         *  === Key points ===
         *  1. The AuthorizedClientServiceOAuth2AuthorizedClientManager integrates and orchestrates:
         *    - ClientRegistrationRepository (stores static client definitions - one in this case)
         *    - OAuth2AuthorizedClientProvider (knows how to request tokens)
         *    - OAuth2AuthorizedClientService (caches them)
         *  2. The principal string ("system" here) acts as the cache key alongside the clientRegistrationId.
         *     Maybe clientId would be better for that (see DefendevTokenCustomizer).
         *     With authorization code grant the principal would be the logged-in user (each having cache
         *     of their own authorizedClient).
         *  3. subsequent calls, if the token is still valid, it’s reused. If expired, Spring will fetch a new one.
         *
         */

        final OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId("53d14824")
            .principal("system")
            .build();
        for (int i=0; i<500; i += 1) {
            /*
             * To observe what happens, enable 'trace' logging for 'org.springframework.security' in
             * the 005-spring-authz-server
             */
            final OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);
            final Instant expiresAt = authorizedClient.getAccessToken().getExpiresAt();
            final String tokenValue = authorizedClient.getAccessToken().getTokenValue();

            System.out.println("expiresAt = " + expiresAt);
            System.out.println("tokenValue = " + tokenValue);
            Thread.sleep(Duration.ofSeconds(15));
        }
    }



}
