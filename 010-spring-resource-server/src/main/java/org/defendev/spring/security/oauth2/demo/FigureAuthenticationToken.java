package org.defendev.spring.security.oauth2.demo;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;



public class FigureAuthenticationToken extends AbstractAuthenticationToken {

    private final FigureUser principal;

    private final Jwt jwtAccessToken;

    public FigureAuthenticationToken(FigureUser principal, Jwt jwtAccessToken,
                                     Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        setAuthenticated(true);
        this.principal = principal;
        this.jwtAccessToken = jwtAccessToken;
    }

    @Override
    public Object getCredentials() {
        return jwtAccessToken;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

}
