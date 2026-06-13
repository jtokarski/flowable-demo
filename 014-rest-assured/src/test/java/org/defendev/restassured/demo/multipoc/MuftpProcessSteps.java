package org.defendev.restassured.demo.multipoc;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static io.restassured.RestAssured.given;



@Component
public class MuftpProcessSteps {

    private final MultipocUriBuilders uriBuilders;

    @Autowired
    public MuftpProcessSteps(MultipocUriBuilders uriBuilders) {
        this.uriBuilders = uriBuilders;
    }

    public JsonPath initializeMuftpProcess(String accessToken) {
        return given()
            .auth().oauth2(accessToken)
            .accept(ContentType.JSON)
            .when()
            .post(uriBuilders.MUFTP_PROCESS_INITIALIZE_PROCESS_UC.toUriString())
            .then()
            .statusCode(200)
            .extract().jsonPath();
    }

    public JsonPath initializeMuftpProcess(String accessToken, ZonedDateTime timeTravelTo) {
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
            .post(uriBuilders.MUFTP_PROCESS_INITIALIZE_PROCESS_UC.toUriString())
            .then()
            .statusCode(200)
            .extract().jsonPath();
    }

    public JsonPath submitDraft(String processInstanceId, String accessToken) {
        final String uriString = uriBuilders.MUFTP_PROCESS_SUBMIT_DRAFT_UC.expand(
            Map.of("processInstanceId", processInstanceId)
        ).toUriString();
        return given()
            .auth().oauth2(accessToken)
            .accept(ContentType.JSON)
            .when()
            .post(uriString)
            .then()
            .statusCode(200)
            .extract().jsonPath();
    }

    public JsonPath cancelProcess(String processInstanceId, String accessToken) {
        final String uriString = uriBuilders.MUFTP_PROCESS_CANCEL_PROCESS_UC.expand(
            Map.of("processInstanceId", processInstanceId)
        ).toUriString();
        return given()
            .auth().oauth2(accessToken)
            .accept(ContentType.JSON)
            .when()
            .post(uriString)
            .then()
            .statusCode(200)
            .extract().jsonPath();
    }

}
