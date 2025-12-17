package org.defendev.restassured.demo.multipoc;

import io.restassured.path.json.JsonPath;
import org.defendev.restassured.demo.junit.extension.AccessToken;
import org.defendev.restassured.demo.junit.extension.ResourceServerAuthzExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.defendev.restassured.demo.junit.extension.Actor.user_1;



@ExtendWith(ResourceServerAuthzExtension.class)
public class TimeTravelTest {

    @Test
    public void initializeProcessInThePast(@AccessToken(user_1) String accessToken1) {
        final ZonedDateTime timeTravelTo = ZonedDateTime.parse("2024-04-15T07:45:00Z", DateTimeFormatter.ISO_DATE_TIME);
        final JsonPath postProcessResponse = MuftpProcessSteps.initializeMuftpProcess(accessToken1, timeTravelTo);
        final String processInstanceId = postProcessResponse.getString("processInstanceId");
        assertThat(processInstanceId).isNotNull();

        final JsonPath processInsightResponse = EmergencyActionSteps.queryMuftpProcessInsight(processInstanceId,
            accessToken1);

        System.out.println("processInsightResponse = " + processInsightResponse);
        // todo: decent assertions
    }

}
