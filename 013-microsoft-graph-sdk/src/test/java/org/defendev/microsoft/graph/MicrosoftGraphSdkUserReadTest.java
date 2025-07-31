package org.defendev.microsoft.graph;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.core.authentication.AzureIdentityAuthenticationProvider;
import com.microsoft.graph.core.requests.GraphClientFactory;
import com.microsoft.graph.models.User;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;



public class MicrosoftGraphSdkUserReadTest {

    private static final String TENANT_ID = "00000000-0000-0000-0000-00000000000a";
    private static final String CLIENT_ID = "00000000-0000-0000-0000-00000000000b";
    private static final String CLIENT_SECRET = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaZ";

    @Test
    public void getUserListDefault() {
        /*
         *  Note copied from example on Microsoft website:
         *
         *  The client credentials flow requires that you request the
         *  /.default scope, and pre-configure your permissions on the
         *  app registration in Azure. An administrator must grant consent
         *  to those permissions beforehand.
         *
         */
        final String[] scopes = new String[] { "https://graph.microsoft.com/.default" };

        final ClientSecretCredential credential = new ClientSecretCredentialBuilder()
            .tenantId(TENANT_ID)
            .clientId(CLIENT_ID)
            .clientSecret(CLIENT_SECRET)
            .build();

        final GraphServiceClient graphClient = new GraphServiceClient(credential, scopes);

        final List<User> userList = graphClient.users().get().getValue();

        assertThat(userList).isNotEmpty();
    }

    @Test
    public void getUserListWithQueryParamsAndLogging() {
        final String[] scopes = new String[] { "https://graph.microsoft.com/.default" };
        final ClientSecretCredential credential = new ClientSecretCredentialBuilder()
            .tenantId(TENANT_ID)
            .clientId(CLIENT_ID)
            .clientSecret(CLIENT_SECRET)
            .build();
        final AzureIdentityAuthenticationProvider authProvider =
            new AzureIdentityAuthenticationProvider(credential, null, scopes);

        /*
         * Creating custom OkHttpClient so that I can use Interceptor to take a look at resulting URL
         * for HTTP call.
         *
         */
        final OkHttpClient httpClient = GraphClientFactory.create()
            .addInterceptor(new LoggingOkInterceptor())
            .build();

        final GraphServiceClient graphClient = new GraphServiceClient(authProvider, httpClient);

        /*
         * Apparently the Microsoft Graph SDK doesn't offer convenient way for querying objects.
         *
         */
        final List<User> userList = graphClient.users()
            .get(requestConfig  -> {
                requestConfig.queryParameters.select = new String[] {"displayName", "jobTitle"};
                requestConfig.queryParameters.filter = "startsWith(givenName, 'lu') OR startsWith(givenName, 'jo')";
            })
            .getValue();

        assertThat(userList).isNotEmpty();
    }

}
