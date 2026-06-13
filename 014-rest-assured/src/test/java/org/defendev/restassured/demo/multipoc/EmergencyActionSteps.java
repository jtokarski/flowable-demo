package org.defendev.restassured.demo.multipoc;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static io.restassured.RestAssured.given;



@Component
public class EmergencyActionSteps {

    private final MultipocUriBuilders uriBuilders;

    @Autowired
    public EmergencyActionSteps(MultipocUriBuilders uriBuilders) {
        this.uriBuilders = uriBuilders;
    }

    public JsonPath queryMuftpProcessInsight(String processInstanceId, String accessToken) {
        final String uriString = uriBuilders.EMERGENCY_ACTION_PROCESS_INSIGHT_UC.expand(
            Map.of("processInstanceId", processInstanceId)
        ).toUriString();
        return given()
            .auth().oauth2(accessToken)
            .accept(ContentType.JSON)
            .when()
            .get(uriString)
            .then()
            .statusCode(200)
            .extract().jsonPath();
    }

}
