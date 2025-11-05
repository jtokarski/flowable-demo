package org.defendev.flowable.demo.multipoc.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;



/*
 * This is not primary demo of ServerDiscriminatorFilter!
 * For primary source of truth see subproject 010-spring-resource-server
 *
 */
public class ServerDiscriminatorFilter extends OncePerRequestFilter {

    private static final Logger log = LogManager.getLogger();

    private static final String X_SERVER_DISCRIMINATOR = "X-Server-Discriminator";

    private final String discriminator;

    public ServerDiscriminatorFilter(String discriminator) {
        this.discriminator = discriminator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException
    {
        response.setHeader(X_SERVER_DISCRIMINATOR, discriminator);
        filterChain.doFilter(request, response);
    }

}
