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
            .post("http://localhost:8014/multipoc/muftp/process")
            .then()
            .statusCode(200)
            .extract().jsonPath();
    }

    public static JsonPath initializeMuftpProcess(String accessToken, ZonedDateTime timeTravelTo) {
        return given()
            .auth().oauth2(accessToken)
            .accept(ContentType.JSON)
            .headers(Map.of(
                "X-Time-Travel-Target", timeTravelTo.format(DateTimeFormatter.ISO_INSTANT),
                "X-Time-Travel-Reference", ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT)
            ))
            .when()
            .post("http://localhost:8014/multipoc/muftp/process")
            .then()
            .statusCode(200)
            .extract().jsonPath();
    }

}
