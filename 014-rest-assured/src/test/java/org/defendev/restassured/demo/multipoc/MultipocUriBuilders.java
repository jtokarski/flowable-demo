package org.defendev.restassured.demo.multipoc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;



/*
 * Spring UriComponents as the main URL templating engine for scratchpad-like projects.
 *
 * - there is some templating in RestAssured itself, but I deliberately decided to use UriComponents
 *   as it's more feature-complete and portable to application code.
 * - name of this class (MultipocUriBuilders) - is a convention itself. There can be one or more like this:
 *     Backend1UriBuilders, Backend2UriBuilders, ...
 * - what should be the form of base URL? Host, port, protocol stored as separate properties? No.
 *   This should be one property and should not have the slash / at the end - I want to rely on UriComponents
 *   to handle this.
 * - remember that UriComponentsBuilder is for building the templates (UriComponents objects),
 *   not for building the URLs. UriComponentsBuilder objects are not thread-safe
 * - UriComponents objects are immutable
 * - note the _UC suffix convention in UriComponents variables
 * - the UriComponents are named after @Controller(s), not being strict on black-box approach here
 *
 */
@Component
public class MultipocUriBuilders {

    public final UriComponents BASE_UC;

    public final UriComponents EMERGENCY_ACTION_PROCESS_INSIGHT_UC;

    public final UriComponents MUFTP_PROCESS_INITIALIZE_PROCESS_UC;

    public final UriComponents MUFTP_PROCESS_SUBMIT_DRAFT_UC;

    public final UriComponents MUFTP_PROCESS_CANCEL_PROCESS_UC;

    @Autowired
    public MultipocUriBuilders(@Value("${multipoc.base-url}") String baseUrl) {

        BASE_UC = UriComponentsBuilder.fromUriString(baseUrl).build();

        EMERGENCY_ACTION_PROCESS_INSIGHT_UC = UriComponentsBuilder.newInstance().uriComponents(BASE_UC)
            .pathSegment("muftp", "process", "{processInstanceId}", "insight", "")
            .build();

        MUFTP_PROCESS_INITIALIZE_PROCESS_UC = UriComponentsBuilder.newInstance().uriComponents(BASE_UC)
            .pathSegment("muftp", "process", "_initialize")
            .build();

        MUFTP_PROCESS_SUBMIT_DRAFT_UC = UriComponentsBuilder.newInstance().uriComponents(BASE_UC)
            .pathSegment("muftp", "process", "{processInstanceId}", "draft", "_submit")
            .build();

        MUFTP_PROCESS_CANCEL_PROCESS_UC = UriComponentsBuilder.newInstance().uriComponents(BASE_UC)
            .pathSegment("muftp", "process", "{processInstanceId}", "_cancel")
            .build();
    }

}
