package org.defendev.spring.cloud.gateway.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.bind.annotation.RequestMethod.GET;



@RequestMapping(path = { "/confidential-spa/api" })
@Controller
public class ConfidentialApiController {

    @RequestMapping(method = GET, path = { "shallow" })
    public Mono<ResponseEntity<ShallowDto>> getShallowInfo() {
        return ReactiveSecurityContextHolder.getContext()
            .map( securityContext -> {
                final int randomInt = ThreadLocalRandom.current().nextInt(1_000, 10_000);
                final Authentication authentication = securityContext.getAuthentication();
                assertThat(authentication)
                    .as("authentication expected to be instance of OAuth2AuthenticationToken")
                    .isInstanceOf(OAuth2AuthenticationToken.class);
                final Object principal = authentication.getPrincipal();
                assertThat(principal)
                    .as("principal expected to be instance of DefaultOAuth2User")
                    .isInstanceOf(DefaultOAuth2User.class);
                final String shallowKnowledge = format(
                    "The ranodm int is %d. The authentication is %s, and the principal is %s",
                    randomInt, authentication.getClass().getCanonicalName(), principal.getClass().getCanonicalName());
                final ShallowDto dto = new ShallowDto(shallowKnowledge);
                return ResponseEntity.ok(dto);
            } );
    }

}
