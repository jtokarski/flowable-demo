package org.defendev.flowable.demo.multipoc.service;

import org.defendev.flowable.demo.multipoc.config.FigureUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;



@Service
public class SpringSecurityContextService implements SecurityContextService {

    @Override
    public String getUserId() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        final Authentication authentication = securityContext.getAuthentication();
        final FigureUser principal = (FigureUser) authentication.getPrincipal();
        return principal.getUserId();
    }

}
