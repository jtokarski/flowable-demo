package org.defendev.restassured.demo.multipoc;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.defendev.restassured.demo.junit.extension.AccessToken;
import org.defendev.restassured.demo.junit.extension.ResourceServerAuthzExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.defendev.restassured.demo.junit.extension.Actor.user_1;



@ExtendWith(ResourceServerAuthzExtension.class)
public class EmergencyActionTest {

    @Test
    public void shouldProvideProcessInsight(@AccessToken(user_1) String accessToken1) {
        final JsonPath processInsight = given()
            .auth().oauth2(accessToken1)
            .accept(ContentType.JSON)
            .when()
            .get("http://localhost:8014/multipoc/muftp/process/{processInstanceId}/insight", 4)
            .then()
            .statusCode(200)
            .extract().jsonPath();

        final boolean unfinished = processInsight.getBoolean("unfinished");
        final boolean exists = processInsight.getBoolean("exists");
        final String financialTransactionId = processInsight.getString("financialTransactionId");

        assertThat(financialTransactionId).isNotNull();
    }

}
