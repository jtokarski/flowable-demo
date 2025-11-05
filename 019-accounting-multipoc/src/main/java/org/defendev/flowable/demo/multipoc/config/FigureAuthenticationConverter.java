package org.defendev.flowable.demo.multipoc.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;



@Component
public class FigureAuthenticationConverter implements Converter<Jwt, FigureAuthenticationToken> {

    private static final Logger log = LogManager.getLogger();

    @Override
    public FigureAuthenticationToken convert(Jwt jwt) {
        final String sub = jwt.getClaimAsString("sub");
        final String firstName = jwt.getClaimAsString("given_name");
        final String lastName = jwt.getClaimAsString("family_name");
        final String email = jwt.getClaimAsString("upn");
        return new FigureAuthenticationToken(new FigureUser(sub, firstName, lastName, email), jwt, List.of());
    }

}
