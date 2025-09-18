package org.defendev.spring.security.oauth2.demo;

import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.proc.JWSAlgorithmFamilyJWSKeySelector;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.JWTClaimsSetAwareJWSKeySelector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Key;
import java.util.List;

import static java.util.Objects.isNull;



@Component
public class DefendevJwsKeySelector implements JWTClaimsSetAwareJWSKeySelector<SecurityContext> {

    private static final Logger log = LogManager.getLogger();

    private final String azureIss;

    private final String azureJwksUri;

    private final String sprin6authzIss;

    private final String sprin6authzJwksUri;

    private JWSKeySelector<SecurityContext> azureKeySelector;

    private JWSKeySelector<SecurityContext> sprin6authzKeySelector;

    @Autowired
    public DefendevJwsKeySelector(
        @Value("${defendev.resource-server.oidc.azure.iss}") String azureIss,
        @Value("${defendev.resource-server.oidc.azure.jwks-uri}") String azureJwksUri,
        @Value("${defendev.resource-server.oidc.sprin6authz.iss}") String sprin6authzIss,
        @Value("${defendev.resource-server.oidc.sprin6authz.jwks-uri}") String sprin6authzJwksUri
    ) {
        this.azureIss = azureIss;
        this.azureJwksUri = azureJwksUri;
        this.sprin6authzIss = sprin6authzIss;
        this.sprin6authzJwksUri = sprin6authzJwksUri;
    }

    @Override
    public List<? extends Key> selectKeys(JWSHeader header, JWTClaimsSet claimSet, SecurityContext context) throws KeySourceException {
        final Object iss = claimSet.getClaim("iss");
        if (iss instanceof String issuer) {
            if (azureIss.equals(issuer)) {
                return getAzureKeySelector().selectJWSKeys(header, context);
            } else if (sprin6authzIss.equals(issuer)) {
                return getSprin6authzKeySelector().selectJWSKeys(header, context);
            }
        }
        return List.of();
    }

    private JWSKeySelector<SecurityContext> getAzureKeySelector() {
        if (isNull(azureKeySelector)) {
            try {
                final URL azureJwksUrl = new URI(azureJwksUri).toURL();
                azureKeySelector = JWSAlgorithmFamilyJWSKeySelector.fromJWKSetURL(azureJwksUrl);
            } catch (MalformedURLException | URISyntaxException | KeySourceException e) {
                throw new IllegalStateException(e);
            }
        }
        return azureKeySelector;
    }

    private JWSKeySelector<SecurityContext> getSprin6authzKeySelector() {
        if (isNull(sprin6authzKeySelector)) {
            try {
                final URL sprin6authzJwksUrl = new URI(sprin6authzJwksUri).toURL();
                sprin6authzKeySelector = JWSAlgorithmFamilyJWSKeySelector.fromJWKSetURL(sprin6authzJwksUrl);
            } catch (MalformedURLException | URISyntaxException | KeySourceException e) {
                throw new IllegalStateException(e);
            }
        }
        return sprin6authzKeySelector;
    }

}
