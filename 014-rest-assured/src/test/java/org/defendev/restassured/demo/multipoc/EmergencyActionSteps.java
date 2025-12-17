package org.defendev.restassured.demo.multipoc;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;



public class EmergencyActionSteps {

    public static JsonPath queryMuftpProcessInsight(String processInstanceId, String accessToken) {
        return given()
            .auth().oauth2(accessToken)
            .accept(ContentType.JSON)
            .when()
            .get("http://localhost:8014/multipoc/muftp/process/{processInstanceId}/insight", processInstanceId)
            .then()
            .statusCode(200)
            .extract().jsonPath();
    }

}
