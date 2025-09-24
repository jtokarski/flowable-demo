package org.defendev.spring.security.oauth2.demo;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;



@Component
public class FigureAuthenticationConverter implements Converter<Jwt, FigureAuthenticationToken> {

    private static final Logger log = LogManager.getLogger();

    private final String azureIss;

    private final String sprin6authzIss;

    @Autowired
    public FigureAuthenticationConverter(
        @Value("${defendev.resource-server.oidc.azure.iss}") String azureIss,
        @Value("${defendev.resource-server.oidc.sprin6authz.iss}") String sprin6authzIss
    ) {
        Validate.notBlank(azureIss);
        Validate.notBlank(sprin6authzIss);
        this.azureIss = azureIss;
        this.sprin6authzIss = sprin6authzIss;
    }

    @Override
    public FigureAuthenticationToken convert(Jwt jwt) {
        /*
         * In the scenario of Resource Server Multi-Tenancy, the most reasonable and recommended approach
         * is to distinguish tenants by the 'iss' token claim.
         *
         */
        final String issuer = jwt.getClaimAsString("iss");
        if (issuer.equals(azureIss)) {
            final String oid = jwt.getClaimAsString("oid");
            final String firstName = jwt.getClaimAsString("given_name");
            final String lastName = jwt.getClaimAsString("family_name");
            final String email = jwt.getClaimAsString("upn");
            return new FigureAuthenticationToken(
                new FigureUser(oid, firstName, lastName, email, null, null),
                jwt,
                List.of()
            );
        } else if (issuer.equals(sprin6authzIss)) {
            final String sub = jwt.getClaimAsString("sub");
            final String firstName = jwt.getClaimAsString("given_name");
            final String lastName = jwt.getClaimAsString("family_name");
            final String email = jwt.getClaimAsString("upn");
            return new FigureAuthenticationToken(
                new FigureUser(sub, firstName, lastName, email, null, null),
                jwt,
                List.of()
            );
        } else if (isBlank(issuer)) {
            throw new IllegalStateException("The iss claim is empty");
        } else {
            throw new IllegalArgumentException("Unrecognized issuer in iss token claim");
        }
    }

}
