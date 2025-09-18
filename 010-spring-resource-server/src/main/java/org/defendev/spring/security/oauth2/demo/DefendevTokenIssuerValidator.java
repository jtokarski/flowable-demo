package org.defendev.spring.security.oauth2.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.stereotype.Component;

import java.util.Set;



@Component
public class DefendevTokenIssuerValidator implements OAuth2TokenValidator<Jwt> {

    private static final Logger log = LogManager.getLogger();

    private final OAuth2TokenValidator<Jwt> delegate;

    private final Set<String> validIssuer;

    public DefendevTokenIssuerValidator(
        @Value("${defendev.resource-server.oidc.azure.iss}") String azureIss,
        @Value("${defendev.resource-server.oidc.sprin6authz.iss}") String sprin6authzIss
    ) {
        delegate = new JwtClaimValidator<Object>("iss", this::issCondition);
        validIssuer = Set.of(azureIss, sprin6authzIss);
    }

    private boolean issCondition(Object issClaim) {
        return issClaim instanceof String issuer && validIssuer.contains(issuer);
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        return delegate.validate(token);
    }

}
