package org.defendev.spring.security.oauth2.demo;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;



@EnableWebSecurity
@Configuration // Without @Configuration, the @EnableWebSecurity doesn't make effect
               // (error that bean HttpSecurity doesn't exist)
public class WebSecurity {

    /*
     * This turned out very difficult with Spring Security to configure the login URL to be "/sign-in"
     * and still use o.s.s.w.a.u.DefaultLoginPageGeneratingFilter as the "/login"
     * is too strongly hardcoded (see o.s.s.c.a.w.c.FormLoginConfigurer,
     *   o.s.s.c.a.w.c.AbstractAuthenticationFilterConfigurer)
     *
     * The change was only possible after implementing my own loginPage.th.html.
     *
     */
    public static final String SIGN_IN_PATH = "/sign-in";

    public static final String CLIENT_CREDENTIALS_GRANT_CLIENT_ID_1 = "950f60";

    /*
     * How do I know the @Scope of some bean class (e.g. HttpSecurity) provided by some @Enable... annotation
     * or some @AutoConfiguration?
     *
     * 1. I can dig into the source code and see that
     *   - @EnableWebSecurity imports HttpSecurityConfiguration class
     *   - the HttpSecurityConfiguration has factory method
     *         @Bean(HTTPSECURITY_BEAN_NAME)
     *         @Scope("prototype")
     *         HttpSecurity httpSecurity() throws Exception {
     *           ...
     *
     * 2. I can do something like below ...
     *
     * See: https://stackoverflow.com/questions/24755304/how-to-find-out-the-scope-of-a-spring-managed-bean
     */
    @Bean
    public Map<String, String> justChecking(ConfigurableApplicationContext applicationContext) {
        final String[] names = applicationContext.getBeanFactory().getBeanDefinitionNames();
        final List<String> likeHttpSecurity = Arrays.stream(names)
            .filter(name -> name.toLowerCase().contains("httpsecurity"))
            .toList();
        assertThat(likeHttpSecurity).isNotEmpty();
        final BeanDefinition httpSecurityBeanDefinition = applicationContext.getBeanFactory().getBeanDefinition(
            "org.springframework.security.config.annotation.web.configuration.HttpSecurityConfiguration.httpSecurity"
        );
        assertThat(httpSecurityBeanDefinition).isNotNull();
        assertThat(httpSecurityBeanDefinition.getScope()).isEqualTo("prototype");
        assertThat(httpSecurityBeanDefinition.getFactoryBeanName())
            .isEqualTo("org.springframework.security.config.annotation.web.configuration.HttpSecurityConfiguration");
        assertThat(httpSecurityBeanDefinition.getFactoryMethodName()).isEqualTo("httpSecurity");
        return Map.of();
    }

    /*
     * This SecurityFilterChain does not have .formLogin() at all - it immediately delegates to the second
     * one (defaultSecurityFilterChain).
     *
     */
    @Order(1)
    @Bean
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        final OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
            OAuth2AuthorizationServerConfigurer.authorizationServer();
        final RequestMatcher protocolEndpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        http
            .securityMatcher(protocolEndpointsMatcher)
            .with(
                authorizationServerConfigurer,
                (authorizationServer) -> authorizationServer
                    .oidc(Customizer.withDefaults())  // Enable OpenID Connect 1.0
                    .authorizationConsentService(new LaxOAuth2AuthorizationConsentService())
            )
            .authorizeHttpRequests(
                customizer -> customizer.anyRequest().authenticated()
            )
            // Redirect to the login page when not authenticated from the
            // authorization endpoint
            .exceptionHandling(
                customizer -> customizer.defaultAuthenticationEntryPointFor(
                    new LoginUrlAuthenticationEntryPoint(SIGN_IN_PATH),
                    new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                )
            );
        return http.build();
    }

    @Order(2)
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher(PathPatternRequestMatcher.withDefaults().matcher("/**"))
            .authorizeHttpRequests(customizer -> customizer
                .requestMatchers(HttpMethod.GET, "/actuator").permitAll()
                .requestMatchers(HttpMethod.GET, "/actuator/health").permitAll()
                .requestMatchers(HttpMethod.GET, "/actuator/info").permitAll()
                .requestMatchers(HttpMethod.GET, "/top-secret/private-key").permitAll()
                .requestMatchers(HttpMethod.GET, "/top-secret/private-key-all").permitAll()
                .requestMatchers(HttpMethod.GET, "/info/").permitAll()
                .requestMatchers(HttpMethod.GET, "/logout-success/").permitAll()
                .requestMatchers(HttpMethod.GET, SIGN_IN_PATH).permitAll()
                .requestMatchers(HttpMethod.POST, SIGN_IN_PATH).permitAll()
                .requestMatchers(HttpMethod.GET, "styles.css", "loginPage.js").permitAll()
                .anyRequest().authenticated()
            )
            // Form login handles the redirect to the login page from the
            // authorization server filter chain
            .exceptionHandling(customizer -> customizer
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(SIGN_IN_PATH))
            )
            .formLogin(customizer -> customizer
                .loginProcessingUrl(SIGN_IN_PATH)
                /*
                 * Why not .successHandler(new SimpleUrlAuthenticationSuccessHandler("/dashboard/")) ?
                 * Because it would break the Authorization Code flow.
                 *
                 */
                .defaultSuccessUrl("/dashboard/")
                .failureHandler(new SimpleUrlAuthenticationFailureHandler(SIGN_IN_PATH))
            )
            .logout(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //
        // Borrowed from o.s.s.c.u.User.withDefaultPasswordEncoder()
        //
        final PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder;
    }

    @Bean
    public ImaginaryUserService imaginaryUserService(PasswordEncoder passwordEncoder) {
        return new ImaginaryUserService(passwordEncoder);
    }

    @Bean
    public UserDetailsService userDetailsService(ImaginaryUserService imaginaryUserService) {
        return new BetterInMemoryUserDetailsService(imaginaryUserService);
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        return new DefendevTokenCustomizer();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        final RegisteredClient easygoWeb = RegisteredClient.withId("19f14aca-66f9-4bb5-b49f-868f420a7c3b")
            .clientId("kttV2w1Zk9")
            .clientSecret("{noop}jv2a1Hacf1h9Pm4")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri("https://localhost:8443/easygo-web/login/oauth2/code/sprin6authz")
            .postLogoutRedirectUri("http://127.0.0.1:8080/")
            .scope(OidcScopes.OPENID)
            .scope(OidcScopes.PROFILE)
            .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
            .build();

        final RegisteredClient defendevGateway = RegisteredClient.withId("e9c7bfa9-84a7-4366-a983-0b347eccb27c")
            .clientId("hZ519F1t6V")
            .clientSecret("{noop}Hj2acmv3a1f0P2h")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri("http://localhost:8011/webcntx/login/oauth2/code/sprin6authz")
            .postLogoutRedirectUri("http://127.0.0.1:8011/")
            .scope(OidcScopes.OPENID)
            .scope(OidcScopes.PROFILE)
            .tokenSettings(TokenSettings.builder()
                // presumable defaults specified explicitly:
                .accessTokenTimeToLive(Duration.ofMinutes(5))
                .refreshTokenTimeToLive(Duration.ofMinutes(60))
                // in some ZCorpo I had:
                //  .accessTokenTimeToLive(Duration.ofHours(2))
                //  .refreshTokenTimeToLive(Duration.ofHours(4))
                .build())
            .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
            .build();

        final RegisteredClient coinne = RegisteredClient.withId("7a19078e-186d-458e-87c8-9706898c7d08")
            .clientId(CLIENT_CREDENTIALS_GRANT_CLIENT_ID_1)
            .clientSecret("{noop}034a75a60fa9e34c17bb4c3521")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .build();

        return new InMemoryRegisteredClientRepository(easygoWeb, defendevGateway, coinne);
    }

    @Bean
    public DefendevGeneratedKeys generatedKeys() {
        /*
         * This bean generates key pair that will be used to sign Access Tokens
         * and will be visible under 'jwks_uri' (http://localhost:8010/defendev-authz/oauth2/jwks)
         *
         * There will be one key pair, but I made this explicit to remember that there is
         * possibility to specify multiple.
         *
         */
        return new DefendevGeneratedKeys(1);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(DefendevGeneratedKeys generatedKeys) {
        final List<JWK> rsaKeys = generatedKeys.getKeyData().stream().map(generatedKeyPair -> {
            RSAPublicKey publicKey = (RSAPublicKey) generatedKeyPair.keyPair().getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) generatedKeyPair.keyPair().getPrivate();
            RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(generatedKeyPair.keyId())
                .build();
            return (JWK) rsaKey;
        }).toList();
        final JWKSet jwkSet = new JWKSet(rsaKeys);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

}
