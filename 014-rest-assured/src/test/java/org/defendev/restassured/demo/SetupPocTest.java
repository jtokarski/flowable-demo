package org.defendev.restassured.demo;

import org.defendev.restassured.demo.config.AccessToken;
import org.defendev.restassured.demo.config.ResourceServerAuthzExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;



@ExtendWith(ResourceServerAuthzExtension.class)
public class SetupPocTest {

    @Test
    public void should(@AccessToken String accessToken) {

        final String string =
            given()
                .auth().oauth2(accessToken)
                .get("http://localhost:8012/backweb/hello").asString();


        System.out.println("string = " + string);
    }

}
