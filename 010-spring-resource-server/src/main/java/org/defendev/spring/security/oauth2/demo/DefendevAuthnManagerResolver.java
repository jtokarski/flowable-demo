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
        DefendevOpaqueTokenIntrospector tokenIntrospector
    ) {
        final ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        jwtProcessor.setJWTClaimsSetAwareJWSKeySelector(keySelector);
        final NimbusJwtDecoder jwtDecoder = new NimbusJwtDecoder(jwtProcessor);
        jwtDecoder.setJwtValidator(tokenIssuerValidator);

        // JwtAuthenticationConverter - this is the default one (explicite). Not used but leaving for reference.
        final Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter =
            new JwtAuthenticationConverter();
        final Converter<Jwt, ? extends AbstractAuthenticationToken> figureAuthenticationConverter =
            new FigureAuthenticationConverter();

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
