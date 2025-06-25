package org.defendev.spring.cloud.gateway.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.WebSession;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;
import static java.util.Objects.nonNull;



@Controller
public class LastVisitedController {

    private static final String LAST_VISITED_KEY = "lastVisited";

    /*
     *
     * ... / Spring WebFlux / Annotated Controllers / Handler Methods / Method Arguments
     * https://docs.spring.io/spring-framework/reference/web/webflux/controller/ann-methods/arguments.html
     *
     */
    @RequestMapping(path = {"api/last-visited"}, method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> getLastVisited(WebSession session) {
        final Map<String, Object> sessionAttributes = session.getAttributes();
        final ZonedDateTime savedLastVisited = (ZonedDateTime) sessionAttributes.get(LAST_VISITED_KEY);
        final ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Z"));
        sessionAttributes.put(LAST_VISITED_KEY, now);

        final Map<String, String> dto = Map.of(
            LAST_VISITED_KEY,
            nonNull(savedLastVisited) ? savedLastVisited.format(ISO_INSTANT) : "-"
        );
        return ResponseEntity.ok(dto);
    }

}
