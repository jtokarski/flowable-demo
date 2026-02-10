package org.defendev.restassured.demo;

import com.github.benmanes.caffeine.cache.LoadingCache;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.defendev.restassured.demo.junit.extension.AccessToken;
import org.defendev.restassured.demo.junit.extension.AccessTokenCache;
import org.defendev.restassured.demo.junit.extension.Actor;
import org.defendev.restassured.demo.junit.extension.ResourceServerAuthzExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.defendev.restassured.demo.junit.extension.Actor.user_1;
import static org.defendev.restassured.demo.junit.extension.Actor.user_2;



/*
 * Seems that, the best approach to verifying JSON response is by .extract().jsonPath():
 *   - allows to verify multiple paths of single response without writing DTOs for full deserialization
 *   - know API of Groovy's GPath
 *      https://github.com/rest-assured/rest-assured/wiki/Usage#json-using-jsonpath
 *      https://groovy-lang.org/processing-xml.html#_gpath
 *
 */
@ExtendWith(ResourceServerAuthzExtension.class)
public class SetupPocTest {

    @Test
    public void shouldHandleMultipleUsers(
        @AccessToken(user_1) String accessToken1,
        @AccessToken(user_2) String accessToken2
    ) {
        final JsonPath helloResponseUser1 = given()
            .auth().oauth2(accessToken1)
            .accept(ContentType.JSON)
            .when()
            .get("http://localhost:8012/backweb/hello")
            .then()
            .statusCode(200)
            .extract().jsonPath();

        assertThat(helloResponseUser1.getString("deepInsight")).contains("010");
        assertThat(helloResponseUser1.getString("userId")).isEqualTo(user_1.getSubject());

        final JsonPath helloResponseUser2 = given()
            .auth().oauth2(accessToken2)
            .accept(ContentType.JSON)
            .when()
            .get("http://localhost:8012/backweb/hello")
            .then()
            .statusCode(200)
            .extract().jsonPath();

        assertThat(helloResponseUser2.getString("deepInsight")).contains("010");
        assertThat(helloResponseUser2.getString("userId")).isEqualTo(user_2.getSubject());
    }

    @Test
    public void shouldWorkWithCache(@AccessTokenCache LoadingCache<Actor, String> accessTokenCache) {
        final String accessToken = accessTokenCache.get(user_2);

        final JsonPath helloResponse = given()
            .auth().oauth2(accessToken)
            .accept(ContentType.JSON)
            .when()
            .get("http://localhost:8012/backweb/hello")
            .then()
            .statusCode(200)
            .extract().jsonPath();

        assertThat(helloResponse.getString("deepInsight")).contains("010");
        assertThat(helloResponse.getString("userId")).isEqualTo(user_2.getSubject());

        final JsonPath animalsResponse = given()
            .log().all(true) //
                             // Logging of the whole request (e.g. in case I need to reply it in another
                             // HTTP client utility)
                             //
            .auth().oauth2(accessToken)
            .accept(ContentType.JSON)
            .when()
            .get("http://localhost:8012/backweb/animals")
            .then()
            .log().ifValidationFails(LogDetail.BODY, true) //
                                                           // Logging of response body in case the assertion fails
                                                           //
            .statusCode(200)
            .extract().jsonPath();

        final List<Map> animals = animalsResponse.getList("$", Map.class);
        assertThat(animals).hasSize(6);
        assertThat(animalsResponse.getBoolean("[0].isPet")).isFalse();
        assertThat((Boolean) animalsResponse.get("[4].isPet")).isNull();

        /*
         * Some more interesting GPath examples (note the find vs findAll)
         *
         */
        final List<String> youngLions = animalsResponse
            .param("species", "Panthera tigris (tiger)")
            .param("ageLimit", 7)
            .getList("findAll { species == it.species && ageLimit > it.age}.name", String.class);
        assertThat(youngLions).hasSize(2).containsExactlyElementsOf(List.of("Luna", "Peugeot"));

        final Map someLion = animalsResponse
            .param("name", "Muffin")
            .getMap("find { name == it.name }");
        assertThat(someLion.get("species")).isEqualTo("Panthera tigris (tiger)");
        assertThat(someLion.get("name")).isEqualTo("Muffin");
        assertThat(someLion.get("isPet")).isEqualTo(false);
    }

}
