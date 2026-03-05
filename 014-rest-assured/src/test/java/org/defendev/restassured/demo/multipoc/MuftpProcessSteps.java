package org.defendev.restassured.demo.multipoc;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static io.restassured.RestAssured.given;



public class MuftpProcessSteps {

    public static JsonPath initializeMuftpProcess(String accessToken) {
        return given()
            .auth().oauth2(accessToken)
            .accept(ContentType.JSON)
            .when()
            .post("http://localhost:8014/multipoc/muftp/process/_initialize")
            .then()
            .statusCode(200)
            .extract().jsonPath();
    }

    public static JsonPath initializeMuftpProcess(String accessToken, ZonedDateTime timeTravelTo) {
        return given()
            .auth().oauth2(accessToken)
            .accept(ContentType.JSON)
            /*
             * Invocation of .headers() adds to what is already accumulated in the builder. Therefore,
             * I can invoke it multiple times.
             */
            .headers(Map.of(
                "X-Time-Travel-Target", timeTravelTo.format(DateTimeFormatter.ISO_INSTANT)
            ))
            .headers(Map.of(
                "X-Time-Travel-Reference", ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT)
            ))
            .headers(Map.of())  // Does not rest headers, just no effect. This is to demonstrate once again that
                                // invocation of .headers() is additive.
            .when()
            .post("http://localhost:8014/multipoc/muftp/process/_initialize")
            .then()
            .statusCode(200)
            .extract().jsonPath();
    }

    public static JsonPath submitDraft(String processInstanceId, String accessToken) {
        return given()
            .auth().oauth2(accessToken)
            .accept(ContentType.JSON)
            .when()
            .post("http://localhost:8014/multipoc/muftp/process/{processInstanceId}/draft/_submit", processInstanceId)
            .then()
            .statusCode(200)
            .extract().jsonPath();
    }

    public static JsonPath cancelProcess(String processInstanceId, String accessToken) {
        return given()
            .auth().oauth2(accessToken)
            .accept(ContentType.JSON)
            .when()
            .post("http://localhost:8014/multipoc/muftp/process/{processInstanceId}/_cancel", processInstanceId)
            .then()
            .statusCode(200)
            .extract().jsonPath();
    }

}
