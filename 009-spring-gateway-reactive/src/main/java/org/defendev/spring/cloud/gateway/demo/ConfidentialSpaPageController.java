package org.defendev.spring.cloud.gateway.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;

import java.time.ZonedDateTime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;



@RequestMapping(path = {
    "/confidential-spa",
    "/confidential-spa/profile"
})
@Controller
public class ConfidentialSpaPageController {

    @RequestMapping(method = GET, path = "/")
    public Rendering confidentialMainPage() {
        return Rendering
            .view("confidential-spa/index.th")
            .modelAttribute("weekAgo", ZonedDateTime.now().minusWeeks(1).format(ISO_LOCAL_DATE))
            .build();
    }

}
