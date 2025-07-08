package org.defendev.spring.cloud.gateway.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.WebSession;

import java.time.ZonedDateTime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static org.apache.commons.lang3.StringUtils.appendIfMissing;
import static org.defendev.spring.cloud.gateway.demo.ClassNameAbbreviation.abbreviatedFqcn;
import static org.springframework.web.bind.annotation.RequestMethod.GET;



@RequestMapping(path = {
    "/confidential-spa",
    "/confidential-spa/profile"
})
@Controller
public class ConfidentialSpaPageController {

    @RequestMapping(method = GET, path = "/")
    public Rendering confidentialMainPage(@CurrentSecurityContext SecurityContext securityContext) {
        final Authentication authentication = securityContext.getAuthentication();
        final Object principal = authentication.getPrincipal();

        final String authenticationClass = abbreviatedFqcn(authentication);
        final String principalClass = abbreviatedFqcn(principal);
        final String subject;
        if (principal instanceof DefaultOidcUser oidcUser) {
            subject = oidcUser.getSubject();
        } else {
            subject = "-";
        }

        return Rendering
            .view("confidential-spa/index.th")
            .modelAttribute("weekAgo", ZonedDateTime.now().minusWeeks(1).format(ISO_LOCAL_DATE))
            .modelAttribute("authenticationClass", authenticationClass)
            .modelAttribute("principalClass", principalClass)
            .modelAttribute("subject", subject)
            .build();
    }

    @RequestMapping(method = GET, path = "session-invalidate")
    public ResponseEntity<Void> assfdfas(
        WebSession session,
        @Value("${spring.webflux.base-path}") String basePath
    ) {
        session.invalidate().block();
        final HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.LOCATION, appendIfMissing(basePath, "/") + "confidential-spa/");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

}
