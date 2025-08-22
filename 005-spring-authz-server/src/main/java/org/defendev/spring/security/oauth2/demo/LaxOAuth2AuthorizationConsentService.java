package org.defendev.spring.security.oauth2.demo;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;



public class LaxOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

    private static class KeepImports {
        InMemoryOAuth2AuthorizationConsentService a;
    }

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        // intentionally do nothing
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        // intentionally do nothing
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        /*
         * How did I know how to build the OAuth2AuthorizationConsent ?
         *   -> breakpoint in o.s.s.o.s.a.InMemoryOAuth2AuthorizationConsentService
         *
         */
        final OAuth2AuthorizationConsent consent = OAuth2AuthorizationConsent
            .withId(registeredClientId, principalName)
            .authority(new SimpleGrantedAuthority("SCOPE_" + OidcScopes.OPENID))
            .authority(new SimpleGrantedAuthority("SCOPE_" + OidcScopes.PROFILE))
            .build();
        return consent;
    }

}
