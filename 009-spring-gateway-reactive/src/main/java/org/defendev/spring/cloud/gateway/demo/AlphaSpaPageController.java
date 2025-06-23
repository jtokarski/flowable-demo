package org.defendev.spring.cloud.gateway.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
    public Rendering defendevPage() {
        return Rendering
            .view("alpha-spa/index.th")
            .modelAttribute("yearsAgo", ZonedDateTime.now().minusYears(15).format(DateTimeFormatter.ISO_LOCAL_DATE))
            .build();
    }

}
