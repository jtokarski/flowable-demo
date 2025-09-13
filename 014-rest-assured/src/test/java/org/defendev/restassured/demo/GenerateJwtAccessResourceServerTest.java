package org.defendev.restassured.demo;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.Test;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static io.restassured.RestAssured.get;
import static org.assertj.core.api.Assertions.assertThat;



public class GenerateJwtAccessResourceServerTest {

    @Test
    public void shouldGenerateSignedJwtAndAccessResourceServer() {


        final byte[] privateKeyBytes = get("http://localhost:8010/defendev-authz/top-secret/private-key").asByteArray();

        try {
            final PKCS8EncodedKeySpec privateSpecLoaded = new PKCS8EncodedKeySpec(privateKeyBytes);
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            final RSAPrivateKey privateKeyLoaded = (RSAPrivateKey) keyFactory.generatePrivate(privateSpecLoaded);

            assertThat(privateKeyLoaded).isNotNull();

            final Algorithm algorithm = Algorithm.RSA256(null, privateKeyLoaded);

            final String token = JWT.create()
                .withSubject("user123")
                .withExpiresAt(Instant.now().plus(4, ChronoUnit.HOURS))
                .sign(algorithm);

            /*
             *
             * ------------------------------------
             *     This is not finished yet
             * ------------------------------------
             *
             *
             */
            System.out.println("Generated JWT: " + token);
        } catch (NoSuchAlgorithmException e) {

        } catch (InvalidKeySpecException e) {

        }
    }

}
