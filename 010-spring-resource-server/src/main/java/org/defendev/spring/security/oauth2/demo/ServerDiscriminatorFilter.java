package org.defendev.spring.security.oauth2.demo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;



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
        /*
         * The response.setHeader() has to go first. This is because after filterChain.doFilter(),
         * the response may be already "commited" (see org.apache.catalina.connector.ResponseFacade).
         * In that case the header is just ignored without the notice.
         *
         */
        response.setHeader(X_SERVER_DISCRIMINATOR, discriminator);
        filterChain.doFilter(request, response);
    }

}
