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
public class EmergencyActionTest {

    @Test
    public void shouldProvideProcessInsight(
        @Autowired EmergencyActionSteps emergencyActionSteps,
        @AccessToken(user_1) String accessToken
    ) {
        final JsonPath processInsight = emergencyActionSteps.queryMuftpProcessInsight("4", accessToken);

        final boolean unfinished = processInsight.getBoolean("unfinished");
        final boolean exists = processInsight.getBoolean("exists");
        final String financialTransactionId = processInsight.getString("financialTransactionId");

        assertThat(financialTransactionId).isNotNull();
    }

}
