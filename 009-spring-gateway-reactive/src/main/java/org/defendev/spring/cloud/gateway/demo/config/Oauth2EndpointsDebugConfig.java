package org.defendev.spring.cloud.gateway.demo.config;


import io.netty.handler.logging.LogLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.client.endpoint.JwtBearerGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2ClientCredentialsGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2RefreshTokenGrantRequest;
import org.springframework.security.oauth2.client.endpoint.ReactiveOAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.TokenExchangeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.WebClientReactiveAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.WebClientReactiveClientCredentialsTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.WebClientReactiveJwtBearerTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.WebClientReactiveRefreshTokenTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.WebClientReactiveTokenExchangeTokenResponseClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;



/*
 *
 * Why logger categories are like Oed_OAuth2AuthorizationCode, Oed_OAuth2RefreshToken, ... ?
 * So that, class name abbreviation doesn't eat any part and they are text-searchable.
 *
 */
@Profile("oauth2Debug")
@Configuration
public class Oauth2EndpointsDebugConfig {

    @Bean
    public ReactiveOAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> authCodeClient() {
        final String category = "org.defendev.spring.cloud.gateway.demo.config.oed.Oed_OAuth2AuthorizationCode";
        final WebClient webClient = buildVerboseWebClient(category);
        final WebClientReactiveAuthorizationCodeTokenResponseClient responseClient =
            new WebClientReactiveAuthorizationCodeTokenResponseClient();
        responseClient.setWebClient(webClient);
        return responseClient;
    }

    @Bean
    public ReactiveOAuth2AccessTokenResponseClient<OAuth2RefreshTokenGrantRequest> refreshTokenClient() {
        final String category = "org.defendev.spring.cloud.gateway.demo.config.oed.Oed_OAuth2RefreshToken";
        final WebClient webClient = buildVerboseWebClient(category);
        final WebClientReactiveRefreshTokenTokenResponseClient responseClient =
            new WebClientReactiveRefreshTokenTokenResponseClient();
        responseClient.setWebClient(webClient);
        return responseClient;
    }

    @Bean
    public ReactiveOAuth2AccessTokenResponseClient<OAuth2ClientCredentialsGrantRequest> clientCredentialsClient() {
        final String category = "org.defendev.spring.cloud.gateway.demo.config.oed.Oed_OAuth2ClientCredentials";
        final WebClient webClient = buildVerboseWebClient(category);
        final WebClientReactiveClientCredentialsTokenResponseClient responseClient =
            new WebClientReactiveClientCredentialsTokenResponseClient();
        responseClient.setWebClient(webClient);
        return responseClient;
    }

    @Bean
    public ReactiveOAuth2AccessTokenResponseClient<JwtBearerGrantRequest> JwtBearerClient() {
        final String category = "org.defendev.spring.cloud.gateway.demo.config.oed.Oed_JwtBearer";
        final WebClient webClient = buildVerboseWebClient(category);
        final WebClientReactiveJwtBearerTokenResponseClient responseClient =
            new WebClientReactiveJwtBearerTokenResponseClient();
        responseClient.setWebClient(webClient);
        return responseClient;
    }

    @Bean
    public ReactiveOAuth2AccessTokenResponseClient<TokenExchangeGrantRequest> tokenExchangeClient() {
        final String category = "org.defendev.spring.cloud.gateway.demo.config.oed.Oed_TokenExchange";
        final WebClient webClient = buildVerboseWebClient(category);
        final WebClientReactiveTokenExchangeTokenResponseClient responseClient =
            new WebClientReactiveTokenExchangeTokenResponseClient();
        responseClient.setWebClient(webClient);
        return responseClient;
    }

    private WebClient buildVerboseWebClient(String category) {
        /*
         * Remember to enable particular logger categories on corresponding level!
         *
         */
        final HttpClient nettyHttpClient = HttpClient.create()
            .wiretap(category, LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);
        final ClientHttpConnector connector = new ReactorClientHttpConnector(nettyHttpClient);
        return WebClient.builder().clientConnector(connector).build();
    }

}
