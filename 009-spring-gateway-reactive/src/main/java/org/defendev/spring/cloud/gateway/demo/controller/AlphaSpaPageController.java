package org.defendev.spring.cloud.gateway.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;



/*
 * The o.s.w.r.r.v.Rendering is reactive equivalent of o.s.w.servlet.ModelAndView
 * It is handled by o.s.w.reactive.result.view.ViewResolutionResultHandler which has lowest precedence
 * See: https://docs.spring.io/spring-framework/reference/web/webflux/dispatcher-handler.html#webflux-resulthandling
 *
 */
@RequestMapping(path = {
    "alpha-spa",
    "alpha-spa/profile"
})
@Controller
public class AlphaSpaPageController {

    @RequestMapping(method = GET, path = "/")
    public Rendering alphaMainPage() {
        return Rendering
            .view("alpha-spa/index.th")
            .modelAttribute("yearsAgo", ZonedDateTime.now().minusYears(15).format(ISO_LOCAL_DATE))
            .build();
    }

    /*
     * Demonstrate that ViewResolutionResultHandler handles controller method return types of:
     *   - Rendering as well as
     *   - Mono<Rendering>
     *
     */
    @RequestMapping(method = GET, path = "/mono")
    public Mono<Rendering> alphaOtherPage() {
        final Rendering rendering = Rendering.view("alpha-spa/mono.th")
            .modelAttribute("yearsForward", ZonedDateTime.now().plusYears(30).format(ISO_LOCAL_DATE))
            .build();
        return Mono.just(rendering);
    }

}
