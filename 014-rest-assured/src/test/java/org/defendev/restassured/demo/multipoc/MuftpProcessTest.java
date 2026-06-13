package org.defendev.restassured.demo.multipoc;

import io.restassured.path.json.JsonPath;
import org.defendev.restassured.demo.RestAssuredContextLoader;
import org.defendev.restassured.demo.RestAssuredProfile;
import org.defendev.restassured.demo.junit.extension.AccessToken;
import org.defendev.restassured.demo.junit.extension.ResourceServerAuthzExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.defendev.restassured.demo.junit.extension.Actor.user_1;



@ActiveProfiles({RestAssuredProfile.local})
@ContextConfiguration(
    classes = {MultipocConfig.class},
    loader = RestAssuredContextLoader.class
)
@ExtendWith({ResourceServerAuthzExtension.class, SpringExtension.class})
public class MuftpProcessTest {

    @Test
    public void shouldInitializeMuftpProcess(
        @Autowired MuftpProcessSteps muftpProcessSteps,
        @AccessToken(user_1) String accessToken
    ) {
        // given

        // when
        final JsonPath initializedMuftp = muftpProcessSteps.initializeMuftpProcess(accessToken);

        final String processInstanceId = initializedMuftp.getString("processInstanceId");
        assertThat(processInstanceId).isNotNull();
    }

    @Test
    public void shouldSubmitDraft(
        @Autowired MuftpProcessSteps muftpProcessSteps,
        @AccessToken(user_1) String accessToken
    ) {
        // given
        final String processInstanceId = "4";

        // when
        final JsonPath submittedDraft = muftpProcessSteps.submitDraft(processInstanceId, accessToken);

        // then
        assertThat(submittedDraft.getString("processInstanceId")).isNotNull();
    }

    @Test
    public void shouldCancelProcess(
        @Autowired MuftpProcessSteps muftpProcessSteps,
        @AccessToken(user_1) String accessToken
    ) {
        // given
        final String processInstanceId = "4";

        // when
        final JsonPath cancelledMuftp = muftpProcessSteps.cancelProcess(processInstanceId, accessToken);

        // then
        assertThat(cancelledMuftp.getString("processInstanceId")).isNotNull();
    }

}
