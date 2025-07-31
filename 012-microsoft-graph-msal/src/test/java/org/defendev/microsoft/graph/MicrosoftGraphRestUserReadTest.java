package org.defendev.microsoft.graph;

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.IClientSecret;
import com.microsoft.aad.msal4j.IConfidentialClientApplication;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;



public class MicrosoftGraphRestUserReadTest {

    private static final String TENANT_ID = "00000000-0000-0000-0000-00000000000a";
    private static final String CLIENT_ID = "00000000-0000-0000-0000-00000000000b";
    private static final String CLIENT_SECRET = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaZ";

    @Test
    public void getUserListDefault() throws MalformedURLException, ExecutionException, InterruptedException, URISyntaxException {

        final IClientSecret clientSecret = ClientCredentialFactory.createFromSecret(CLIENT_SECRET);
        final String authority = "https://login.microsoftonline.com/" + TENANT_ID + "/";

        final IConfidentialClientApplication application = ConfidentialClientApplication
            .builder(CLIENT_ID, clientSecret)
            .authority(authority)
            .build();

        final ClientCredentialParameters clientCredentialParameters = ClientCredentialParameters
            .builder(Set.of("https://graph.microsoft.com/.default"))
            .build();

        /*
         * The ConfidentialClientApplication maintains access token cache and is inteded for reuse.
         *
         */
        final IAuthenticationResult authResult = application.acquireToken(clientCredentialParameters).get();

        final String accessToken = authResult.accessToken();


        final RestClient restClient = RestClient.builder().messageConverters(converters -> {
            /*
             * Here, I'm not configuring message converters. All I do is just assert that
             * the MappingJackson2HttpMessageConverter is among preconfigured converters. Otherwise,
             * I would expect problems with proper working of Jackson anootations @JsonCreator and @JsonProperty
             * on DTOs (UserResponseDto and UserDto).
             *
             */
            assertThat(converters).isNotEmpty();
            final Optional<HttpMessageConverter<?>> any = converters.stream()
                .filter(httpMessageConverter -> httpMessageConverter instanceof MappingJackson2HttpMessageConverter)
                .findAny();
            assertThat(any).isNotEmpty();
            final List<MediaType> supportedMediaTypes = any.get().getSupportedMediaTypes();
            assertThat(supportedMediaTypes).contains(MediaType.APPLICATION_JSON);
        }).build();

        final UserResponseDto userResponseDto = restClient.get()
            .uri(new URI("https://graph.microsoft.com/v1.0/users"))
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .body(new ParameterizedTypeReference<UserResponseDto>() {
            });

        assertThat(userResponseDto).isNotNull();
        assertThat(userResponseDto.getValue()).isNotEmpty();
        assertThat(userResponseDto.getValue().getFirst().getGivenName()).isNotBlank();
    }

}
