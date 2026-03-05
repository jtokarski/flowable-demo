package org.defendev.restassured.demo.multipoc;

import io.restassured.path.json.JsonPath;
import org.defendev.restassured.demo.junit.extension.AccessToken;
import org.defendev.restassured.demo.junit.extension.ResourceServerAuthzExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.defendev.restassured.demo.junit.extension.Actor.user_1;



@ExtendWith(ResourceServerAuthzExtension.class)
public class MuftpProcessTest {

    @Test
    public void shouldInitializeMuftpProcess(@AccessToken(user_1) String accessToken) {
        // given

        // when
        final JsonPath initializedMuftp = MuftpProcessSteps.initializeMuftpProcess(accessToken);

        final String processInstanceId = initializedMuftp.getString("processInstanceId");
        assertThat(processInstanceId).isNotNull();
    }

    @Test
    public void shouldSubmitDraft(@AccessToken(user_1) String accessToken) {
        // given
        final String processInstanceId = "16";

        // when
        final JsonPath submittedDraft = MuftpProcessSteps.submitDraft(processInstanceId, accessToken);

        // then
        assertThat(submittedDraft.getString("processInstanceId")).isNotNull();
    }

    @Test
    public void shouldCancelProcess(@AccessToken(user_1) String accessToken) {
        // given
        final String processInstanceId = "4";

        // when
        final JsonPath cancelledMuftp = MuftpProcessSteps.cancelProcess(processInstanceId, accessToken);

        // then
        assertThat(cancelledMuftp.getString("processInstanceId")).isNotNull();
    }

}
