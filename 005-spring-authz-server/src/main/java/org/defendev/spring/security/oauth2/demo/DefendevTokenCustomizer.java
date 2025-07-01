package org.defendev.spring.security.oauth2.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;



public class DefendevTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private static final Logger log = LogManager.getLogger();

    @Override
    public void customize(JwtEncodingContext context) {
        if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
            final Authentication authentication = context.getPrincipal();
            final Object principal = authentication.getPrincipal();
            if (principal instanceof ImaginaryUser imaginaryUser) {
                context.getClaims()
                    .claim("oid", imaginaryUser.getAccessTokenClaims().oid())
                    .claim("upn", imaginaryUser.getAccessTokenClaims().upn())
                    .claim("given_name", imaginaryUser.getAccessTokenClaims().givenName())
                    .claim("family_name", imaginaryUser.getAccessTokenClaims().familyName())
                    .claim("groups", imaginaryUser.getAccessTokenClaims().groups());
            } else {
                final String principalInfo = ofNullable(principal)
                    .map(p -> p.getClass().getCanonicalName())
                    .orElse("null");
                throw new IllegalStateException(
                    format("Principal was [%s] Only supporting ImaginaryUser", principalInfo)
                );
            }
        }
    }

}
