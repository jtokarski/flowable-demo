package org.defendev.spring.security.oauth2.demo;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.ThreadLocalRandom;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.bind.annotation.RequestMethod.GET;



@Controller
public class ExempliGratiaController {

    @RequestMapping(method = GET, path = "hello")
    public ResponseEntity<DeepDto> hello(HttpEntity<Object> httpEntity) {

        final SecurityContext securityContext = SecurityContextHolder.getContext();
        final Authentication authentication = securityContext.getAuthentication();
        final Object principal = authentication.getPrincipal();
        assertThat(authentication).isInstanceOf(FigureAuthenticationToken.class);
        assertThat(principal).isInstanceOf(FigureUser.class);

        assertThat(httpEntity.getHeaders().get(HttpHeaders.AUTHORIZATION))
            .isNotNull()
            .hasSize(1);
        assertThat(httpEntity.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
            .startsWith("Bearer ");

        final int randomInt = ThreadLocalRandom.current().nextInt(10_000, 20_000);
        final DeepDto dto = new DeepDto(format("This is random number from 010 Resource Server: %d", randomInt));
        return ResponseEntity.ok(dto);
    }

}
