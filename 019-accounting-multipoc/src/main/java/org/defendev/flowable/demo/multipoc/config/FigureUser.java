package org.defendev.flowable.demo.multipoc.config;

import static java.util.Objects.nonNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.util.Collection;
import java.util.List;
import java.util.Map;



public class FigureUser implements OAuth2AuthenticatedPrincipal {

    private final String userId;

    private final String firstName;

    private final String lastName;

    private final String email;

    public FigureUser(String userId, String firstName, String lastName, String email) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getName() {
        return userId;
    }

}
