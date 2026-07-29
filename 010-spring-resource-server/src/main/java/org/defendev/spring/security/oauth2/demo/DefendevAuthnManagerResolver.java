package org.defendev.spring.security.oauth2.demo;

import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.stereotype.Component;



@Component
public class DefendevAuthnManagerResolver implements AuthenticationManagerResolver<HttpServletRequest> {

    private static final Logger log = LogManager.getLogger();

    private final AuthenticationManager jwtAuthnManager;

    private final AuthenticationManager opaqueTokenManager;

    @Autowired
    public DefendevAuthnManagerResolver(
        DefendevJwsKeySelector keySelector,
        DefendevTokenIssuerValidator tokenIssuerValidator,
        DefendevOpaqueTokenIntrospector tokenIntrospector,
        FigureAuthenticationConverter figureAuthenticationConverter
    ) {
        /*
         * The Spring Security documentation, when discussing multitenancy in OAuth2 Resource Server,
         * describes the way to avoid parsing JWT token twice:
         * (https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/multitenancy.html#_parsing_the_claim_only_once)
         * This is achieved by "collapsing" the concept of AuthenticationManagerResolver by instantiating
         * AuthenticationProvider capable of internally handling multiple tenants. Specifically,
         * this is JwtAuthenticationProvider backed by NimbusJwtDecoder backed by JWTClaimsSetAwareJWSKeySelector.
         *
         * So why do I still have this AuthenticationManagerResolver then?
         * This is because this double-parse-avoiding technique applies only
         * to tenants using JWT (non-opaque) tokens - sprin6authz and Azure.
         * This AuthenticationManagerResolver is to distinguish into non-opaque/opaque token tenants.
         *
         */
        final ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        jwtProcessor.setJWTClaimsSetAwareJWSKeySelector(keySelector);
        final NimbusJwtDecoder jwtDecoder = new NimbusJwtDecoder(jwtProcessor);
        jwtDecoder.setJwtValidator(tokenIssuerValidator);

        /*
         * JwtAuthenticationConverter - this is the default one (explicite). Not used but leaving for reference.
         * It got replaced by FigureAuthenticationConverter.
         */
        final Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter =
            new JwtAuthenticationConverter();

        final JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(jwtDecoder);
        jwtAuthenticationProvider.setJwtAuthenticationConverter(figureAuthenticationConverter);
        jwtAuthnManager = new ProviderManager(jwtAuthenticationProvider);

        opaqueTokenManager = new ProviderManager(new OpaqueTokenAuthenticationProvider(tokenIntrospector));
    }

    @Override
    public AuthenticationManager resolve(HttpServletRequest request) {
        return isOpaqueTokenRequest(request) ? opaqueTokenManager : jwtAuthnManager;
    }

    private boolean isOpaqueTokenRequest(HttpServletRequest request) {
        return false;
    }

}
