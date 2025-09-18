package org.defendev.spring.security.oauth2.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;



@Component
public class DefendevOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private static final Logger log = LogManager.getLogger();

    private final RestClient restClient;

    private record IntrospectResultDto(@JsonProperty("clientId") String client_id) {}

    @Autowired
    public DefendevOpaqueTokenIntrospector() {
        restClient = RestClient.builder().build();
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        /*
         * RestClient does not support regular Map for APPLICATION_FORM_URLENCODED
         */
        final MultiValueMap<String, String> formData = CollectionUtils.toMultiValueMap(
            Map.of("token", List.of("eyJhbGc...Jp-QV30")));
        final IntrospectResultDto introspectResult = restClient.post()
            .uri("https://...")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(formData)
            .retrieve()
            .body(IntrospectResultDto.class);

        /* Should probably return some implementation of OAuth2AuthenticatedPrincipal */
        return null;
    }

}
